package com.control_horas.horas_trabajo.controllers.web;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.control_horas.horas_trabajo.dtos.ResumenDiaDTO;
import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;


@Controller
public class informeController {
	
	private final RegistroRepository registroRepo;
	private final UsuarioRepository userRepo;
	
	public informeController(RegistroRepository reg, UsuarioRepository user) {
		this.registroRepo = reg;
		this.userRepo = user;
	}
	
	@GetMapping("/informe")
	public String verInforme(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
							@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
							Model model, Principal principal) {
		Usuario u = userRepo.findByUsername(principal.getName()).orElseThrow();
		
		LocalDate inicio = (fechaInicio != null) ? fechaInicio : LocalDate.now().withDayOfMonth(1);
		LocalDate fin = (fechaFin != null) ? fechaFin : LocalDate.now();
		
		// Agrupamos todos los registros por usuario
		List<Registro> registrosUsuario = registroRepo.findAll().stream()
				.filter(r -> r.getUsuario().equals(u))
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> !r.getHoraEntrada().toLocalDate().isBefore(inicio) &&
							 !r.getHoraEntrada().toLocalDate().isAfter(fin))
				.toList();
		Map<LocalDate, List<Registro>> registros = registrosUsuario.stream()
				.collect(Collectors.groupingBy(r -> r.getHoraEntrada().toLocalDate()));
		
		List<ResumenDiaDTO> resumen = registros.entrySet().stream()
				.map(entry -> {
					LocalDate fecha = entry.getKey();
					List<Registro> tramos = entry.getValue();
					tramos.sort(Comparator.comparing(Registro::getHoraEntrada));
					
					LocalTime entrada1 = tramos.size() >= 1 ? tramos.get(0).getHoraEntrada().toLocalTime() : null;
				    LocalTime salida1  = tramos.size() >= 1 ? tramos.get(0).getHoraSalida().toLocalTime() : null;
				    LocalTime entrada2 = tramos.size() >= 2 ? tramos.get(1).getHoraEntrada().toLocalTime() : null;
				    LocalTime salida2  = tramos.size() >= 2 ? tramos.get(1).getHoraSalida().toLocalTime() : null;
				    
				    long minutosTotales = tramos.stream()
						      .mapToLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
						      .sum();
				    return new ResumenDiaDTO(fecha, entrada1, salida1, entrada2, salida2, minutosTotales);
				})
				.sorted(Comparator.comparing(ResumenDiaDTO::getFecha))
				.toList();
		
		model.addAttribute("resumen",resumen);
		model.addAttribute("registros", registros);
		model.addAttribute("mesSeleccionado", inicio.getMonth());
		
		return "informe"; 
	}
	
	@GetMapping("/informe-mensual")
	public String verInformeMensual(@RequestParam(required = false) YearMonth mes,
									Model model, Principal principal) {
		Usuario u = userRepo.findByUsername(principal.getName()).orElseThrow();
		
		if (mes == null) mes = YearMonth.now();
		
		LocalDate inicio = mes.atDay(1);
		LocalDate fin = mes.atEndOfMonth();
		
		List<Registro> registros = registroRepo.findAll().stream()
				.filter(r -> r.getUsuario().equals(u))
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate fecha = r.getHoraEntrada().toLocalDate();
					return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
				})
				.toList();
		
		Map<LocalDate, List<Registro>> registrosPorDia = registros.stream()
				.collect(Collectors.groupingBy(r -> r.getHoraEntrada().toLocalDate()));

		List<ResumenDiaDTO> resumenMensual = registrosPorDia.entrySet().stream()
		  .map(entry -> {
		    LocalDate fecha = entry.getKey();
		    List<Registro> tramos = entry.getValue();

		    tramos.sort(Comparator.comparing(Registro::getHoraEntrada));

		    LocalTime entrada1 = tramos.size() >= 1 ? tramos.get(0).getHoraEntrada().toLocalTime() : null;
		    LocalTime salida1  = tramos.size() >= 1 ? tramos.get(0).getHoraSalida().toLocalTime() : null;
		    LocalTime entrada2 = tramos.size() >= 2 ? tramos.get(1).getHoraEntrada().toLocalTime() : null;
		    LocalTime salida2  = tramos.size() >= 2 ? tramos.get(1).getHoraSalida().toLocalTime() : null;

		    long minutosTotales = tramos.stream()
		      .mapToLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
		      .sum();

		    return new ResumenDiaDTO(fecha, entrada1, salida1, entrada2, salida2, minutosTotales);
		  })
		  .sorted(Comparator.comparing(ResumenDiaDTO::getFecha))
		  .toList();

				

		
		model.addAttribute("resumenMensual", resumenMensual);
		model.addAttribute("mesSeleccionado", mes);
		
		return "informe_mensual";
	}

}
