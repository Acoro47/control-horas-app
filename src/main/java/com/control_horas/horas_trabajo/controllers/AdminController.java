package com.control_horas.horas_trabajo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	@Autowired
	private UsuarioRepository userRepo;
		
	@Autowired
	private BCryptPasswordEncoder encoder; 
	
	@GetMapping("")
	public String adminPanel() {
		return "admin/panel";
	}
	
	@GetMapping("/usuarios")
	public String verTodos(Model model){
		try {
			List<Usuario> usuarios = userRepo.findAll();
			model.addAttribute("usuarios",usuarios);
		}
		catch (Exception e) {
			
			return "redirect:/error";
		}
		
		return "admin/usuarios";
	}
	
	@GetMapping("/estadisticas")
	public String verEstadisticas(Model model) {
		List<Usuario> usuarios = userRepo.findAll();
		long activos = usuarios.stream()
				.filter(Usuario ::isEnabled).count();
		long bloqueados = usuarios.stream()
				.filter(u -> !u.isAccountNonLocked()).count();
		long desactivados = usuarios.stream()
				.filter(u -> !u.isEnabled()).count();
		long admins = usuarios.stream()
				.filter(u -> u.getRol().toString().equals("ADMIN")).count();
		long users = usuarios.stream()
				.filter(u -> u.getRol().toString().equals("USER")).count();
		
		Map<String, Long> stats = Map.of(
				"activos", activos,
				"desactivados",desactivados,
				"bloqueados", bloqueados,
				"admins", admins,
				"users", users
				);
		
		model.addAttribute("stats",stats);
		return "admin/estadisticas";
				
	}
	
	@GetMapping("cambiar-password/{id}")
	public String mostrarFormularioPassword(@PathVariable Long id, Model model) {
		Usuario u = userRepo.findById(id).orElseThrow();
		model.addAttribute("usuario",u);
		return "admin/cambiar-password";
	}
	
	@PostMapping("cambiar-password/{id}")
	public String procesarCambioPassword(@PathVariable Long id,
										@RequestParam String nuevaPassword,
										@RequestParam String confirmarPassword,
										RedirectAttributes redirect) {
		
		if(!nuevaPassword.equals(confirmarPassword)) {
			redirect.addFlashAttribute("mensaje", "❌ Las contraseñas no coinciden");
			return "redirect:/admin/cambiar-password/" + id;
		}
		
		Usuario u = userRepo.findById(id).orElseThrow();
		u.setPassword(encoder.encode(nuevaPassword));
		System.err.println("➡️ Intentando guardar usuario...");
		userRepo.save(u);		
		redirect.addFlashAttribute("mensaje", "✅ Contraseña cambiada correctamente.");
	    return "redirect:/admin/usuarios";
	}
	
	
	@PostMapping("/toggle-enabled/{id}")
	public String alternarUsuarioActivo(@PathVariable Long id, RedirectAttributes redirect) {
		Usuario u = userRepo.findUserById(id);
		u.setEnabled(!u.isEnabled());
		System.err.println("➡️ Intentando guardar usuario...");
		Usuario saved = userRepo.save(u);
		System.err.println("✅ Usuario guardado con ID: " + saved.getId());
		
		return "redirect:/admin/usuarios";
	}
	
	
	@PostMapping("/toggle-lock/{id}")
	public String alternarBloqueoUsuario(@PathVariable Long id, RedirectAttributes redirect) {
		Usuario u = userRepo.findUserById(id);
		u.setAccountNonLocked(!u.isAccountNonLocked());
		System.err.println("➡️ Intentando guardar usuario...");
		Usuario saved = userRepo.save(u);
		System.err.println("✅ Usuario guardado con ID: " + saved.getId());
		
		return "redirect:/admin/usuarios";
	}
	
		
	
}
