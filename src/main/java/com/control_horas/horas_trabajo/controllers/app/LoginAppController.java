package com.control_horas.horas_trabajo.controllers.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.app.ApiResponseDTO;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

@RestController
@RequestMapping("/api")
public class LoginAppController {
	
	private final UsuarioDetailsService userService;
	
	public LoginAppController(UsuarioDetailsService uServ) {
		this.userService = uServ;
	}
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponseDTO> login(
			@RequestParam String username,
			@RequestParam String password
			) {
		boolean valido = userService.validarCredenciales(username, password);
		if (valido) {
			return ResponseEntity.ok(new ApiResponseDTO("success", "✅ Inicio de sesión correcto"));
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponseDTO("error", "❌ Usuario o contraseña incorrectos"));
		}
	}

}
