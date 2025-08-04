package com.control_horas.horas_trabajo.controllers.app;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.control_horas.horas_trabajo.dtos.web.RegistroDTO;
import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.services.RegistroService;
import com.control_horas.horas_trabajo.utils.HoraUtils;



@RestController
@RequestMapping("/api")
public class RegistroAppController {
	
	private final RegistroService regService;
	private final UsuarioRepository userRepo;
	private final RegistroRepository regRepo;
	
	public RegistroAppController(RegistroService reg,UsuarioRepository uRepo,RegistroRepository rRepo) {
		this.regService = reg;
		this.userRepo = uRepo;
		this.regRepo = rRepo;
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
	
	@GetMapping("/registros/hoy")
	public ResponseEntity<List<RegistroDTO>> obtenerRegistrosHoy(
			@RequestParam Long id
			) {
		try {
			LocalDate hoy = LocalDate.now();
			List<RegistroDTO> registros = regService.mapearRegistrosDia(id,hoy);
			return ResponseEntity.ok(registros);
		}
		catch (DateTimeParseException e) {
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	@PostMapping("/entrada")
	public ResponseEntity<?> registrarEntrada(@RequestParam Long idUsuario){
		Usuario u = userRepo.findById(idUsuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
		
		boolean existe = regRepo.existsByUsuarioIdAndHoraSalidaIsNull(idUsuario);
		
		if (existe) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(Map.of("mensaje", "Ya existe una entrada abierta"));
		}
		
		LocalDateTime hora = HoraUtils.redondearMinutos(LocalDateTime.now());
		Registro r = new Registro();
		r.setUsuario(u);
		r.setHoraEntrada(hora);
		regRepo.save(r);
		
		return ResponseEntity.ok(Map.of("mensaje", "✅ Entrada registrada"));
	}
	
	@PostMapping("/salida")
	public ResponseEntity<?> registrarSalida(@RequestParam Long idUsuario){
		Usuario u = userRepo.findById(idUsuario).orElseThrow();
		LocalDateTime hora = HoraUtils.redondearMinutos(LocalDateTime.now());
		
		Registro r = regRepo.findFirstByUsuarioAndHoraSalidaIsNullOrderByHoraEntrada(u).orElseThrow(() -> new RuntimeException("No hay entrada abierta"));
		
		r.setHoraSalida(hora);
		regRepo.save(r);
		
		return ResponseEntity.ok(Map.of("mensaje", "✅ Salida registrada"));
	}

}
