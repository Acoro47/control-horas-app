package com.control_horas.horas_trabajo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	public UsuarioDetailsService userService;
	
	@GetMapping("")
	public String adminPanel() {
		return "admin/panel";
	}
	
	@GetMapping("/usuarios")
	public String verTodos(Model model){
		List<Usuario> usuarios = userService.obtenerTodosUsuarios();
		model.addAttribute("usuarios",usuarios);
		return "admin/usuarios";
	}

	@PostMapping("/reset-password/{id}")
	public String resetearPassword(@PathVariable Long id, RedirectAttributes redirect){
		userService.resetearPassword(id);
		redirect.addFlashAttribute("mensaje", "Contraseña reiniciada");
		return "redirect:/admin/usuarios";
	}
}
