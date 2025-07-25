package com.control_horas.horas_trabajo.controllers.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.app.UsuarioDTO;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

@RestController
@RequestMapping("/api")
public class LoginAppController {
	
	private final UsuarioDetailsService userService;
	
	public LoginAppController(UsuarioDetailsService uServ) {
		this.userService = uServ;
	}
	
	@PostMapping("/login")
	public ResponseEntity<UsuarioDTO> login(
			@RequestParam String username,
			@RequestParam String password
			) {
		boolean valido = userService.validarCredenciales(username, password);
		if (valido) {
			
			Usuario user = userService.obtenerUsuarioPorNombre(username);
			UsuarioDTO userDto = new UsuarioDTO(
					user.getId(), user.getUsername(), "✅ Inicio de sesión correcto"
					);
			
			return ResponseEntity.ok(userDto);
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(null);
		}
	}

}
