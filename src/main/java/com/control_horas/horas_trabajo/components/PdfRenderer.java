package com.control_horas.horas_trabajo.components;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfRenderer {

	public byte[] renderToPdf(String html) {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.createPDF(baos);
			return baos.toByteArray();

		} catch (Exception e) {
			throw new RuntimeException("Error generando PDF", e);
		}
	}

}
