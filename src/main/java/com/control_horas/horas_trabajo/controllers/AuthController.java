package com.control_horas.horas_trabajo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UsuarioRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	
	
	@GetMapping("/registro")
	public String mostrarFormularioRegistro(Model m) {
		
		m.addAttribute("usuario", new Usuario());
		
		return "registro";
	}
	
	
	
	@PostMapping("/guardarUsuario")
	public String registrarUsuario(@ModelAttribute Usuario u,
									@RequestParam String confirmarPassword,
									Model model) {
				
		if (repo.existsByMail(u.getMail()) || repo.existsByUsername(u.getUsername())) {
			model.addAttribute("usuario", u);
			model.addAttribute("error", "El usuario ya existe!!!");
			return "registro";
		}
		else {
			System.out.println("Usuario no existente");
		}
		
		if (!u.getPassword().equals(confirmarPassword)) {
			log.warn("Las contraseñas no coinciden");
			model.addAttribute("usuario", u);
			model.addAttribute("error", "Las contraseñas no coinciden.");
			return "registro";
		}
		
		log.debug("Objeto recibido: " + u.toString());
		
		u.setPassword(encoder.encode(u.getPassword()));
		u.setRol(Role.USER);
		u.setAccountNonExpired(true);
		u.setAccountNonLocked(true);
		u.setCredentialsNonExpired(true);
		u.setEnabled(true);
		
		try {
			repo.save(u);
		}
		catch (Exception e) {
			model.addAttribute("usuario", u);
			model.addAttribute("error", "Las contraseñas no coinciden.");
			return "registro";
		}
		
		
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
