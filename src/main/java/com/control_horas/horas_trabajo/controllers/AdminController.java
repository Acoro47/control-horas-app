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
import org.springframework.web.bind.annotation.RequestParam;
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
		try {
			List<Usuario> usuarios = userService.obtenerTodosUsuarios();
			model.addAttribute("usuarios",usuarios);
		}
		catch (Exception e) {
			
			return "redirect:/error";
		}
		
		return "admin/usuarios";
	}

	@GetMapping("/cambiar-password/{id}")
	public String resetearPassword(@PathVariable Long id, Model model){
		Usuario user = userService.obtenerUsuario(id);
		model.addAttribute("usuario",user);
		
		return "admin/cambiar-password";
	}
	
	@PostMapping("/cambiar-password/{id}")
	public String procesarCambioPassword(@PathVariable Long id, @RequestParam String nuevaPassword, RedirectAttributes redirect) {
		userService.actualizarPassword(id, nuevaPassword);
		redirect.addFlashAttribute("mensaje", "La contraseña fue actualizada con éxito");
		return "redirect:/admin/usuarios";
		
	}
	
	@PostMapping("/toggle-enabled/{id}")
	public String alternarUsuarioActivo(@PathVariable Long id, RedirectAttributes redirect) {
		Usuario u = userService.obtenerUsuario(id);
		u.setEnabled(!u.isEnabled());
		userService.actualizarUsuario(u);
		redirect.addFlashAttribute("mensaje", "Estado del usuario actualizado con éxito");
		return "redirect:/admin/usuarios";
	}
}
