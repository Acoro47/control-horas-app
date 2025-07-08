package com.control_horas.horas_trabajo.utils;

import org.springframework.stereotype.Component;

@Component("formato")
public class FormatoHelper {
	
	public String horasDesdeMinutos(long minutos) {
		long h = minutos / 60;
		long m = minutos % 60;
		return String.format("%d:%02d h", h,m);
	}
	
	public String importeDesdeMinutos(long minutosExtra, double tarifaHora) {
		double euros = (minutosExtra / 60.0) * tarifaHora;
		return String.format("%.2f €", euros);
	}
	
	public String horasContrato(double horasContratoDiarias) {
	    long minutos = (long)(horasContratoDiarias * 60);
	    return horasDesdeMinutos(minutos);
	  }
	
	public String horasExtra(long minutosTotales, double horasContratoDiarias) {
	    long contratoMinutos = (long)(horasContratoDiarias * 60);
	    long extra = Math.max(0, minutosTotales - contratoMinutos);
	    return horasDesdeMinutos(extra);
	  }

	public String importe(long minutosTotales, double horasContratoDiarias, double tarifaHora) {
		long contratoMinutos = (long)(horasContratoDiarias * 60);
		long extra = Math.max(0, minutosTotales - contratoMinutos);
		return importeDesdeMinutos(extra, tarifaHora);
	}

}
