package com.control_horas.horas_trabajo.dtos;

import java.time.LocalDateTime;

public record RegistroDTO(
		LocalDateTime entrada,
		LocalDateTime salida,
		String duracionBase,
		String duracionExtra,
		String duracionTotal
		) {

}
