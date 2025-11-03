package com.control_horas.horas_trabajo.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.control_horas.horas_trabajo.components.PdfRenderer;
import com.control_horas.horas_trabajo.dtos.web.ResumenDiaDTO;
import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.utils.FormatoHelper;


@Service
public class InformeMensualPdfService {
	
	private static final Locale ESP = Locale.forLanguageTag("es");
	

	private final RegistroRepository registroRep;
	private final UsuarioRepository userRepo;
	private final SpringTemplateEngine templateEngine;
	private final PdfRenderer pdfRenderer;
	private final double horasContrato = 4.0;
	private final double tarifa = 9.0;
	private final long contratoDiarioMin = (long)(horasContrato * 60);
	
	public InformeMensualPdfService(RegistroRepository registroRep, UsuarioRepository userRepo,
			SpringTemplateEngine templateEngine, PdfRenderer pdfRenderer) {
		super();
		this.registroRep = registroRep;
		this.userRepo = userRepo;
		this.templateEngine = templateEngine;
		this.pdfRenderer = pdfRenderer;
	}
	
	public byte[] generarPdfUsuario(String username, YearMonth mes) {
		Usuario usuario = userRepo.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
		
		YearMonth target = mes == null ? YearMonth.now() : mes;
		
		LocalDate inicio = target.atDay(1);
		LocalDate fin = target.atEndOfMonth();
		
		List<Registro> registros = resumenMesRegistrosUsuario(usuario, inicio, fin);
		
		List<ResumenDiaDTO> resumenDTO = prepararResumen(registros);
		
		long extrasMesMin = minutosExtrasMes(resumenDTO);
		long extrasFsMin = minutosExtrasFS(resumenDTO);
		
		double importeMES = (extrasMesMin / 60.0) * tarifa;
		double importeFS = (extrasFsMin / 60.0) * tarifa;
		double importeTotal = importeMES + importeFS;
		
		
		FormatoHelper formato = new FormatoHelper();
		String mesLegible = target.getMonth().getDisplayName(java.time.format.TextStyle.FULL, ESP).toUpperCase() + " " + target.getYear();
		
		String estilos = cargarEstilos();
		
		Context ctx = new Context();
		ctx.setVariable("estilosCSS", estilos);
		ctx.setVariable("formato", formato);
		ctx.setVariable("usuario", usuario);
		ctx.setVariable("resumen", resumenDTO);
		ctx.setVariable("hmesFormat", format(extrasMesMin));
		ctx.setVariable("hmfsFormat", format(extrasFsMin));
		ctx.setVariable("horasContrato", horasContrato);
		ctx.setVariable("tarifa", tarifa);
		ctx.setVariable("importeMes", String.format("%.2f", importeMES));
		ctx.setVariable("importeFs", String.format("%.2f", importeFS));
		ctx.setVariable("importeTotal", String.format("%.2f", importeTotal));
		ctx.setVariable("mesSeleccionado", mesLegible);
		
		String html = templateEngine.process("plantilla_pdf", ctx);
		
		return pdfRenderer.renderToPdf(html);
	}
	
	
	protected String cargarEstilos() {
		try {
			ClassPathResource resource = new ClassPathResource("static/css/estilos_pdf.css");
			try (var inputStream = resource.getInputStream()) {
				return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			}
		} catch (IOException e) {
			throw new RuntimeException("No se pudo cargar estilos PDF", e);
		}
	}
	
	private List<Registro> resumenMesRegistrosUsuario(Usuario usuario, LocalDate inicio, LocalDate fin){
		return registroRep.findAll().stream()
				.filter(r -> r.getUsuario().equals(usuario))
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate f = r.getHoraEntrada().toLocalDate();
					return !f.isBefore(inicio) && !f.isAfter(fin);						
				})
				.toList();
		
	}
	
	
	private List<ResumenDiaDTO> prepararResumen(List<Registro> registros){
		return registros.stream()
				.collect(Collectors.groupingBy(r -> r.getHoraEntrada().toLocalDate()))
				.entrySet()
				.stream()
				.map(e -> {
					var lista = e.getValue().stream()
							.sorted((a,b) -> a.getHoraEntrada().compareTo(b.getHoraEntrada())
									).toList();
					var fecha = e.getKey();
					
					var entrada1 = lista.size() > 0 ? lista.get(0).getHoraEntrada().toLocalTime() : null;
					var salida1 = lista.size() > 0 ? lista.get(0).getHoraSalida().toLocalTime() : null;
					
					var entrada2 = lista.size() > 1 ? lista.get(1).getHoraEntrada().toLocalTime() : null;
					var salida2 = lista.size() > 1 ? lista.get(1).getHoraSalida().toLocalTime() : null;
					
					var totalMin = lista.stream()
							.mapToLong(r -> Duration.between(r.getHoraEntrada(),r.getHoraSalida()).toMinutes()).sum();
					
					return new ResumenDiaDTO(fecha,entrada1, salida1, entrada2, salida2, totalMin);
					
				}).sorted((a,b) -> a.getFecha().compareTo(b.getFecha())).toList();
		 
	}
	
	private long minutosExtrasMes(List<ResumenDiaDTO> resumenDTO) {
		
		return resumenDTO.stream()
				.filter(d -> !isFinDeSemanaOFestivo(d.getFecha()))
				.mapToLong(d -> Math.max(0, d.getMinutosTotales() - contratoDiarioMin))
				.sum();
	}
	
	private long minutosExtrasFS(List<ResumenDiaDTO> resumenDTO) {
		
		return resumenDTO.stream()
				.filter(d -> isFinDeSemanaOFestivo(d.getFecha()))
				.mapToLong(d -> Math.max(0, d.getMinutosTotales() - contratoDiarioMin))
				.sum();
	}
	
	
	
	private boolean isFinDeSemanaOFestivo(LocalDate fecha) {
		var d = fecha.getDayOfWeek();
		return d == java.time.DayOfWeek.SATURDAY || d == java.time.DayOfWeek.SUNDAY;
	}
	
	private String format(long minutos) {
	    long h = minutos / 60;
	    long m = minutos % 60;
	    return String.format("%d:%02d h", h, m);    
	}
	
	
}
