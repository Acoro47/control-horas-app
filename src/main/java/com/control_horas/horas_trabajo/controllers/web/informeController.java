package com.control_horas.horas_trabajo.controllers.web;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.control_horas.horas_trabajo.dtos.web.RegistroDTO;
import com.control_horas.horas_trabajo.dtos.web.ResumenDiaDTO;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.services.RegistroService;


@Controller
public class informeController {
	
	private final RegistroService registroServ;
	private final UsuarioRepository userRepo;
	
	public informeController(RegistroService reg, UsuarioRepository user) {
		this.registroServ = reg;
		this.userRepo = user;
	}
	
	@GetMapping("/informe")
	public String verInforme(
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam(required = false) 
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
			Model model, Principal principal) {
		
		Usuario u = userRepo.findByUsername(principal.getName())
				.orElseThrow();
		
		LocalDate inicio = (fechaInicio != null) ? fechaInicio : LocalDate.now()
				.withDayOfMonth(1);
		
		LocalDate fin = (fechaFin != null) ? fechaFin : LocalDate.now();
		
		List<ResumenDiaDTO> resumen = registroServ
				.mapearResumenDiario(u.getId(), inicio, fin);
		List<RegistroDTO> registros = registroServ
				.mapearRegistros(u.getId(), inicio, fin);
		
		model.addAttribute("resumen", resumen);
		model.addAttribute("registros", registros);
		model.addAttribute("mesSeleccionado", inicio.getMonth());
		
		return "informe"; 
	}
	
	@GetMapping("/informe-mensual")
	public String verInformeMensual(@RequestParam(required = false) YearMonth mes,
									Model model, Principal principal) {
		Usuario u = userRepo.findByUsername(principal.getName()).orElseThrow();
		
		if (mes == null) mes = YearMonth.now();
		
		YearMonth month = (mes != null) ? mes : YearMonth.now();
		LocalDate inicio = mes.atDay(1);
		LocalDate fin = mes.atEndOfMonth();
		
		List<ResumenDiaDTO> resumenMensual  = registroServ
				.mapearResumenDiario(u.getId(), inicio, fin);
		
		model.addAttribute("resumenMensual", resumenMensual );
		model.addAttribute("mesSeleccionado", month);
		
				
		return "informe_mensual";
	}

}
