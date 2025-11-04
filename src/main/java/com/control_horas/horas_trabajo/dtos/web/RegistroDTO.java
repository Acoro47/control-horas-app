package com.control_horas.horas_trabajo.dtos.web;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record RegistroDTO(

		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime entrada,

		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime salida,
		String duracionBase,
		String duracionExtra,
		String duracionTotal
		) {

}
