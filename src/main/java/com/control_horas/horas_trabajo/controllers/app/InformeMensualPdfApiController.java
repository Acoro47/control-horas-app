package com.control_horas.horas_trabajo.controllers.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.control_horas.horas_trabajo.dtos.web.RegistroDTO;
import com.control_horas.horas_trabajo.dtos.web.ResumenDiaDTO;
import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.services.RegistroService;
import com.control_horas.horas_trabajo.utils.FormatoHelper;
import com.itextpdf.text.DocumentException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/informe-mensual")
public class InformeMensualPdfApiController {
	
	private static final Logger logger = LoggerFactory.getLogger(InformeMensualPdfApiController.class);
	private static final Locale ESP = Locale.forLanguageTag("es");
		
	private final RegistroRepository registroRep;
	private final RegistroService regService;
	private final UsuarioRepository userRepo;
	private final org.thymeleaf.spring6.SpringTemplateEngine templateEngine;
	private final ResourceLoader loader;
	
	public InformeMensualPdfApiController(
			RegistroRepository reg, 
			UsuarioRepository user, SpringTemplateEngine thymeleafEngine,
			ResourceLoader loader,
			RegistroService regServ) {
		
		this.registroRep = reg;
		this.regService = regServ;
		this.userRepo = user;
		this.templateEngine = thymeleafEngine;
		this.loader = loader;
	}

	
	@GetMapping("/pdf")
	public ResponseEntity<byte[]> exportarPdf(
			@RequestParam(required = false) String mes,
			HttpServletRequest request,
			Principal principal) throws IOException, DocumentException {
		
		logger.debug("Llamada /api/informe-mensual/pdf mes={} desde IP={}",mes, request.getRemoteAddr());
		
		YearMonth mesSeleccionado = (mes != null ? YearMonth.parse(mes, DateTimeFormatter.ofPattern("yyyy-MM")) : YearMonth.now());
		String html = generarHTMLDesdeDatos(mesSeleccionado, principal);
		logger.info("HTML generado (longitud={}):\n{}", html.length(), html);
		byte[] pdfBytes = generarPdfDesdeHtml(html);
		
		String filename = String.format("Informe_%s.pdf", mesSeleccionado);
		
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + filename + "\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdfBytes);
		
	}
	
	private boolean isFinDeSemanaOFestivo(LocalDate fecha) {
		var d = fecha.getDayOfWeek();
		return d == java.time.DayOfWeek.SATURDAY || d == java.time.DayOfWeek.SUNDAY;
	}
	
	private String formatMin(long minutos) {
	    double h = minutos / 60.0;
	    double m = minutos % 60.0;
	    return String.format("%d:%02d h", h, m);
	  }
	
	
	private String generarHTMLDesdeDatos(YearMonth mes, Principal principal) {
		
		LocalDate inicio = mes.atDay(1);
		LocalDate fin = mes.atEndOfMonth();
				
		Usuario usuario = userRepo.findByUsername(principal.getName()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
		
		Long usuarioId = usuario.getId();
		
		List<Registro> registros = registroRep.findAll().stream()
				.filter(r -> r.getUsuario().equals(usuario))
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate f = r.getHoraEntrada().toLocalDate();
					return !f.isBefore(inicio) && !f.isAfter(fin);
				})
				.toList();
		
		List<ResumenDiaDTO> res = regService.mapearResumenDiario(usuarioId, inicio, fin);
		
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
				}).sorted((a,b) -> a.getFecha().compareTo(b.getFecha())
						).toList();
		
		double horasContrato = 4.0;
		double tarifa = 9.0;
		long contratoDiarioMin = (long)(horasContrato * 60);
		
		long extraLaboral = regService.extraerMinutosExtraMesLaboral(res, contratoDiarioMin);
		long extraFinde = regService.extraerMinutosExtraMesFinde(res, contratoDiarioMin);
		
		String extraLaboralString = regService.formatearMinutos(extraLaboral);
		String extraFindeString = regService.formatearMinutos(extraFinde);
		
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
		
		double importeExtraLab = (extraLaboral/60.0) * tarifa;
		double importeExtraFinde = (extraFinde / 60.0) * tarifa;
		double importeTotalExtra = importeExtraLab + importeExtraFinde;
		
		Resource cssRes = loader.getResource("classpath:static/css/estilos_pdf.css");
		try {
			String estilos = new String(cssRes.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			
			Context ctx = new Context(ESP);
			ctx.setVariable("estilosCSS", estilos);
			ctx.setVariable("formato", new FormatoHelper());
			ctx.setVariable("usuario", usuario);
			ctx.setVariable("resumen", res);
			ctx.setVariable("hmesFormat", extraLaboralString);
			ctx.setVariable("hmfsFormat", extraFindeString);
			ctx.setVariable("horasContrato", horasContrato);
			ctx.setVariable("tarifa", tarifa);
			ctx.setVariable("importeMes", String.format("%.2f", importeExtraLab));
			ctx.setVariable("importeFs", String.format("%.2f", importeExtraFinde));
			ctx.setVariable("importeTotal", String.format("%.2f", importeTotalExtra));
			ctx.setVariable("mesSeleccionado", mes.getMonth().getDisplayName(TextStyle.FULL, ESP).toUpperCase() + " " + mes.getYear());
			
			return templateEngine.process("plantilla_pdf", ctx);
			
		} catch (IOException e) {
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					"Error interno al procesar el estilo del pdf"
					);
			
		}
	}

	
	private byte[] generarPdfDesdeHtml(String html) throws IOException, DocumentException {
		
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			try {
				renderer.setDocumentFromString(html);
				renderer.layout();
				renderer.createPDF(baos);
			} catch (Exception e) {
				logger.error("Error generando PDF: {}", e.getMessage(), e);
				throw new IOException("Error interno al generar PDF");
			}
			logger.debug("Generando PDF a partir del HTML...");
			return baos.toByteArray();
		}
	}
}


