package com.control_horas.horas_trabajo.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

@Configuration
public class AuthenticationConfig {
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider(UsuarioDetailsService userService,
															BCryptPasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);
		
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
	
	
	
		
}
