package com.control_horas.horas_trabajo.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.control_horas.horas_trabajo.entities.Usuario;


@Controller
public class AuthController {
		
	
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model m) {
		m.addAttribute("usuario", new Usuario());
		return "registro";
	}
		
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required=false) String error,
			Model model) {
		if (error != null) {
			model.addAttribute("loginError", true);
		}
		
		return "login";
	}
}
