package com.control_horas.horas_trabajo.dtos.web;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ResumenDiaDTO {
	private LocalDate fecha;
	private LocalTime entrada1;
	private LocalTime salida1;
	private LocalTime entrada2;
	private LocalTime salida2;
	private long minutosTotales;

	public ResumenDiaDTO(LocalDate fecha, LocalTime entrada1, LocalTime salida1, LocalTime entrada2, LocalTime salida2, long minutosTotales) {
		this.fecha = fecha;
		this.entrada1 = entrada1;
		this.salida1 = salida1;
		this.entrada2 = entrada2;
		this.salida2 = salida2;
		this.minutosTotales = minutosTotales;
	}

	// Formato
	private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
	private static final Locale ESP = Locale.forLanguageTag("es");



	public long getMinutosTotales() {
		return minutosTotales;
	}

	public void setMinutosTotales(long minutosTotales) {
			this.minutosTotales = minutosTotales;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public String getDiaSemana() {
		return fecha.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, ESP);
	}

	public String getEntrada1Formateada() {
		return entrada1 != null ? entrada1.format(HORA) : "";
	}

	public String getSalida1Formateada() {
		return salida1 != null ? salida1.format(HORA) : "";
	}

	public String getEntrada2Formateada() {
		return entrada2 != null ? entrada2.format(HORA) : "";
	}

	public String getSalida2Formateada() {
		return salida2 != null ? salida2.format(HORA) : "";
	}

	public String getTotalHorasFormateadas() {
		long h = minutosTotales / 60;
		long m = minutosTotales % 60;
		return String.format("%d:%02d h", h, m);
	}

	public String getHorasContratoFormateadas(double horasContrato) {
		long minContrato = (long)(horasContrato * 60);
		long h = minContrato / 60;
		long m = minContrato % 60;
		return String.format("%d:%02d h", h, m);
	}

	public String getHorasExtraFormateadas(double horasContrato) {
		long minContrato = (long)(horasContrato * 60);
		long extra = Math.max(0, minutosTotales - minContrato);
		long h = extra / 60;
		long m = extra % 60;
		return String.format("%d:%02d h", h, m);
	}

	public String getImporteExtras(double tarifaHora, double horasContrato) {
		long minContrato = (long)(horasContrato * 60);
		long extra = Math.max(0, minutosTotales - minContrato);
		double euros = (extra / 60.0) * tarifaHora;
		return String.format("%.2f €", euros);
	}
}
