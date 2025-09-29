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
import com.control_horas.horas_trabajo.dtos.app.LoginErrorResponse;
import com.control_horas.horas_trabajo.dtos.app.LoginResponse;
import com.control_horas.horas_trabajo.dtos.app.TokenResponse;
import com.control_horas.horas_trabajo.entities.LoginRequest;
import com.control_horas.horas_trabajo.entities.Usuario;
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
	public ResponseEntity<LoginResponse> login(
			@Valid @RequestBody LoginRequest datos) {
		
		String username = datos.getUsername();
		String password = datos.getPassword();
		
		boolean valido = userService.validarCredenciales(username, password);
		
		if (!valido) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(new LoginErrorResponse("Credenciales incorrectas", HttpStatus.UNAUTHORIZED.value()));
					
		}
		
		UserDetails userDetails = userService.loadUserByUsername(username);
		String token = jwtService.generateToken(userDetails);
		Usuario usuario = userService.obtenerUsuarioPorNombre(username);
		return ResponseEntity.ok(new TokenResponse(
				usuario.getId(),
				username,
				usuario.getRol().toString(),
				token));
		
	}

}
