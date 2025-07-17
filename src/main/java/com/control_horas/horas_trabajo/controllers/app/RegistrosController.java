package com.control_horas.horas_trabajo.controllers.app;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.web.RegistroDTO;
import com.control_horas.horas_trabajo.services.RegistroService;



@RestController
@RequestMapping("/api")
public class RegistrosController {
	
	private final RegistroService regService;
	
	public RegistrosController(RegistroService reg) {
		this.regService = reg;
	}
	
	@GetMapping("/registros")
	public ResponseEntity<List<RegistroDTO>> obtenerRegistros(
			@RequestParam Long id,
			@RequestParam String fechaDesde,
			@RequestParam String fechaHasta
			) {
		try {
			LocalDate desde = LocalDate.parse(fechaDesde);
			LocalDate hasta = LocalDate.parse(fechaHasta);
			List<RegistroDTO> registros = regService.mapearRegistros(id, desde, hasta);
			return ResponseEntity.ok(registros);
		}
		catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().build();
		}
		
	}

}
