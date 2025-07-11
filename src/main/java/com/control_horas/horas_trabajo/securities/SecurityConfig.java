package com.control_horas.horas_trabajo.securities;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.control_horas.horas_trabajo.components.CustomSuccessHandler;


@Configuration
public class SecurityConfig {
	
	
	@Autowired
	private CustomSuccessHandler successHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/registro","/panel","admin/estadisticas-horas","/guardarUsuario", "/css/**", "/js/**").permitAll()
			.requestMatchers("/solicitar","/solicitar/**").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated())
		.formLogin(form -> form
			.loginPage("/login")
			.successHandler(successHandler)
			.permitAll()
		)
		.logout(logout -> logout.permitAll());
		
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
		throws Exception {
			return config.getAuthenticationManager();
	}
	
	
	

}
