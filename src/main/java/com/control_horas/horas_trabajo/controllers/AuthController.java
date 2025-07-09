package com.control_horas.horas_trabajo.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.control_horas.horas_trabajo.entities.Role;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;

@Controller
public class AuthController {
	
	private final UsuarioRepository repo;
	private final BCryptPasswordEncoder encoder;
	
	public AuthController(UsuarioRepository uRepo, BCryptPasswordEncoder enc) {
		this.encoder = enc;
		this.repo = uRepo;
	}
	
	
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model m) {
		m.addAttribute("usuario", new Usuario());
		
		return "registro";
	}
	
	@PostMapping("/guardarUsuario")
	public String registrarUsuario(@ModelAttribute Usuario u,
									@RequestParam String confirmarPassword,
									Model model) {
		
		if (!u.getPassword().equals(confirmarPassword)) {
			model.addAttribute("usuario", u);
			model.addAttribute("error", "Las contraseñas no coinciden.");
			return "registro";
		}
		
		u.setPassword(encoder.encode(u.getPassword()));
		u.setRol(Role.USER);
		repo.save(u);
		
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required=false) String error, Model model) {
		if (error != null) {
			model.addAttribute("loginError", true);
		}
		return "login";
	}

}
