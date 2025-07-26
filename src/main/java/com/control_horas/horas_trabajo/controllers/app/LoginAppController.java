package com.control_horas.horas_trabajo.controllers.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.app.TokenResponse;
import com.control_horas.horas_trabajo.services.JwtService;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

@RestController
@RequestMapping("/api")
public class LoginAppController {
	
	private final UsuarioDetailsService userService;
	private final JwtService jwtService;
	
	public LoginAppController(UsuarioDetailsService uServ, JwtService jServ) {
		this.userService = uServ;
		this.jwtService = jServ;
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(
			@RequestParam String username,
			@RequestParam String password
			) {
		boolean valido = userService.validarCredenciales(username, password);
		if (valido) {
			UserDetails userDetails = userService.loadUserByUsername(username);
			String token = jwtService.generateToken(userDetails);
			
			return ResponseEntity.ok(new TokenResponse(token));
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.build();
		}
	}

}
