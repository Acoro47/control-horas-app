package com.control_horas.horas_trabajo.controllers.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.app.ErrorResponse;
import com.control_horas.horas_trabajo.dtos.app.TokenResponse;
import com.control_horas.horas_trabajo.entities.LoginRequest;
import com.control_horas.horas_trabajo.services.JwtService;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class LoginAppController {
	
	private final UsuarioDetailsService userService;
	private final JwtService jwtService;
	
	public LoginAppController(UsuarioDetailsService uServ, JwtService jServ) {
		this.userService = uServ;
		this.jwtService = jServ;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(
			@Valid @RequestBody LoginRequest datos) {
		
		String username = datos.getUsername();
		String password = datos.getPassword();
		
		boolean valido = userService.validarCredenciales(username, password);
		
		if (!valido) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ErrorResponse("Invalid credentials"));	
		}
		
		UserDetails userDetails = userService.loadUserByUsername(username);
		String token = jwtService.generateToken(userDetails);
		
		return ResponseEntity.ok(new TokenResponse(
				userService.obtenerUsuarioPorNombre(username).getId(),
				username,
				userService.obtenerUsuarioPorNombre(username).getRol().toString(),
				token));
		
	}

}
