package com.control_horas.horas_trabajo.controllers;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;

@Controller
public class RegistroController {
	
	private final RegistroRepository registroRepo;
	private final UsuarioRepository userRepo;
	
	
	
	public RegistroController(RegistroRepository rRepo, UsuarioRepository uRepo) {
		this.registroRepo = rRepo;
		this.userRepo = uRepo;
	}
	
	@GetMapping("/panel")
	public String verPanel(Model model, Authentication auth) {
		Usuario u = userRepo.findByUsername(auth.getName()).orElseThrow();
		
		boolean hayEntradaActiva = registroRepo
				.findFirstByUsuarioAndHoraSalidaIsNullOrderByHoraEntrada(u)
				.isPresent();
		
		LocalDate hoy = LocalDate.now();
		List<Registro> registrosDeHoy = registroRepo.findAll().stream()
				.filter(r -> r.getUsuario().equals(u))
				.filter(r -> r.getHoraEntrada() != null)
				.filter(r -> r.getHoraEntrada().toLocalDate().equals(hoy))
				.toList();
		
		model.addAttribute("registrosDeHoy", registrosDeHoy);
		model.addAttribute("hayEntradaActiva", hayEntradaActiva);
		model.addAttribute("nombreUsuario", u.getUsername());
		
		return "registro_panel";
	}
	
	
	// Controlador de registro de entrada al trabajo
	
	@PostMapping("/entrada")
	public String registrarEntrada(Authentication auth) {
		
		LocalDateTime entrada = LocalDateTime.now();
		LocalDateTime redondeada = redondearMinutos(entrada);
		
		Usuario u = userRepo.findByUsername(auth.getName()).orElseThrow();
		
		Registro r = new Registro();
	
		r.setHoraEntrada(redondeada);
		r.setUsuario(u);
		registroRepo.save(r);
		
		return "redirect:/panel";
	}
	
	// Controlador de registro de salida al trabajo
	
	@PostMapping("/salida")
	public String registrarSalida(Authentication auth) {
		
		LocalDateTime entrada = LocalDateTime.now();
		LocalDateTime redondeada = redondearMinutos(entrada);
		
		Usuario u = userRepo.findByUsername(auth.getName()).orElseThrow();
		Registro r = registroRepo.findFirstByUsuarioAndHoraSalidaIsNullOrderByHoraEntrada(u)
				.orElseThrow();
		r.setHoraSalida(redondeada);
		registroRepo.save(r);
		
		return "redirect:/panel";
	}
	
	@GetMapping("/")
	public String redirigirRaiz() {
		return "redirect:/panel";
	}
	
	
	public LocalDateTime redondearMinutos(LocalDateTime original) {
		int minutos = original.getMinute();
		
		List<Integer> permitidos = Arrays.asList(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
		int cercano = permitidos.get(0);
		int diferenciaMinima = Math.abs(minutos - cercano);
		
		for (int valor:permitidos) {
			int diferencia = Math.abs(minutos - valor);
			if (diferencia < diferenciaMinima) {
				cercano = valor;
				diferenciaMinima = diferencia;
			}
		}
		
		return original.withMinute(cercano).withSecond(0).withNano(0);
	}
	
	
	

}
