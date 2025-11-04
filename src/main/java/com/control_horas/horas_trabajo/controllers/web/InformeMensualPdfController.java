package com.control_horas.horas_trabajo.controllers.web;

import java.io.IOException;
import java.security.Principal;
import java.time.YearMonth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.control_horas.horas_trabajo.services.InformeMensualPdfService;
import com.itextpdf.text.DocumentException;

@Controller
public class InformeMensualPdfController {

	private final InformeMensualPdfService informeMensualPdfService;

	public InformeMensualPdfController(InformeMensualPdfService informeMensualPdfService) {
		this.informeMensualPdfService = informeMensualPdfService;
	}

	@GetMapping("/informe-mensual/pdf")
	public ResponseEntity<byte[]> exportarPdf(@RequestParam(required = false) YearMonth mes,
											Principal principal) throws IOException, DocumentException {


		byte[] pdf = informeMensualPdfService.generarPdfUsuario(principal.getName(), mes);

		YearMonth target = mes == null ? YearMonth.now() : mes;
		String filename = "Informe_" + target + ".pdf";

		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + filename + "\"")
				.body(pdf);

	}


}
