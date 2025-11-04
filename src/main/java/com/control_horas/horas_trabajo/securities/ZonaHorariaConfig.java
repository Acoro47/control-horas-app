package com.control_horas.horas_trabajo.securities;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class ZonaHorariaConfig {

	@PostConstruct
	public void setZonaHorariaPorDefecto() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"));
	}

}
