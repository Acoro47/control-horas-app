package com.control_horas.horas_trabajo.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.control_horas.horas_trabajo.dtos.ResumenDiaDTO;
import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.utils.FormatoHelper;
import com.itextpdf.text.DocumentException;

@Controller
public class InformeMensualPdfController {
	
	private static final Locale ESP = Locale.forLanguageTag("es");
	
	private final RegistroRepository registroRep;
	private final UsuarioRepository userRepo;
	private final org.thymeleaf.spring6.SpringTemplateEngine templateEngine;
	
	public InformeMensualPdfController(RegistroRepository reg, UsuarioRepository user, org.thymeleaf.spring6.SpringTemplateEngine thymeleafEngine) {
		this.registroRep = reg;
		this.userRepo = user;
		this.templateEngine = thymeleafEngine;
	}
	
	@GetMapping("/informe-mensual/pdf")
	public ResponseEntity<byte[]> exportarPdf(@RequestParam(required = false) YearMonth mes,
											Principal principal) throws IOException, DocumentException {
		
		Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow();
		if (mes == null) mes = YearMonth.now();
		
		LocalDate inicio = mes.atDay(1);
		LocalDate fin = mes.atEndOfMonth();
		
		List<Registro> registros = registroRep.findAll().stream()
				.filter(r -> r.getUsuario().equals(usuario))
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate f = r.getHoraEntrada().toLocalDate();
					return !f.isBefore(inicio) && !f.isAfter(fin);
				})
				.toList();
		
		List<ResumenDiaDTO> resumen = registros.stream()
				.collect(Collectors.groupingBy(r -> r.getHoraEntrada().toLocalDate()))
				.entrySet()
				.stream()
				.map(e -> {
					var lista = e.getValue().stream().sorted((a,b) -> a.getHoraEntrada().compareTo(b.getHoraEntrada())).toList();
					var fecha = e.getKey();
					var entrada1 = lista.size() > 0 ? lista.get(0).getHoraEntrada().toLocalTime() : null;
					var salida1 = lista.size() > 0 ? lista.get(0).getHoraSalida().toLocalTime() : null;
					var entrada2 = lista.size() > 1 ? lista.get(1).getHoraEntrada().toLocalTime() : null;
					var salida2 = lista.size() > 1 ? lista.get(1).getHoraSalida().toLocalTime() : null;
					var totalMin = lista.stream().mapToLong(r -> java.time.Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes()).sum();
					
					return new ResumenDiaDTO(fecha,entrada1, salida1, entrada2, salida2, totalMin);
				}).sorted((a,b) -> a.getFecha().compareTo(b.getFecha())).toList();
		
		
		double horasContrato = 4.0;
		double tarifa = 9.0;
		long contratoDiarioMin = (long)(horasContrato * 60);
		
		long extrasMesMin = resumen.stream()
				.filter(d -> !isFinDeSemanaOFestivo(d.getFecha()))
				.mapToLong(d -> Math.max(0, d.getMinutosTotales() - contratoDiarioMin))
				.sum();
		
		long extrasFsMin = resumen.stream()
				.filter(d -> isFinDeSemanaOFestivo(d.getFecha()))
				.mapToLong(d -> Math.max(0, d.getMinutosTotales() - contratoDiarioMin))
				.sum();
		
		double importeMES = (extrasMesMin/60.0) * tarifa;
		double importeFS = (extrasFsMin / 60.0) * tarifa;
		double importeTotal = importeMES + importeFS;
		
		FormatoHelper formato = new FormatoHelper();
		
		String mesLegible = mes.getMonth().getDisplayName(java.time.format.TextStyle.FULL, ESP).toUpperCase() + " " + mes.getYear();
		
		String estilos = Files.readString(Paths.get("src/main/resources/static/css/estilos_pdf.css"));
		
		Context ctx = new Context();
		ctx.setVariable("estilosCSS", estilos);
		ctx.setVariable("formato", formato);
		ctx.setVariable("usuario", usuario);
		ctx.setVariable("resumen", resumen);
		ctx.setVariable("hmesFormat", format(extrasMesMin));
		ctx.setVariable("hmfsFormat", format(extrasFsMin));
		ctx.setVariable("horasContrato", horasContrato);
		ctx.setVariable("tarifa", tarifa);
		ctx.setVariable("importeMes", String.format("%.2f", importeMES));
		ctx.setVariable("importeFs", String.format("%.2f", importeFS));
		ctx.setVariable("importeTotal", String.format("%.2f", importeTotal));
		ctx.setVariable("mesSeleccionado", mesLegible);
		
		String html = templateEngine.process("plantilla_pdf", ctx);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(baos);
		
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=Informe_" + ".pdf")
				.body(baos.toByteArray());
		
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
