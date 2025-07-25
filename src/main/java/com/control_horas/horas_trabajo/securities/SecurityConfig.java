package com.control_horas.horas_trabajo.securities;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.control_horas.horas_trabajo.components.CustomSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class SecurityConfig {
	
	
	@Autowired
	private CustomSuccessHandler successHandler;
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
			.requestMatchers("/login","/registro","/panel","/guardarUsuario", "/css/**", "/js/**").permitAll()
			.requestMatchers("/solicitar","/solicitar/**").permitAll()
			.requestMatchers("/activar","/activar/**").permitAll()
			.requestMatchers("/api/enviarToken","/api/login","/api/registros","/api/entrada","/api/salida").permitAll()
			.requestMatchers("/api/informe-mensual/pdf").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
			)
		.formLogin(form -> form
			.loginPage("/login")
			.successHandler(successHandler)
			.permitAll()
		)
		.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
			String uri = request.getRequestURI();
			if (uri.startsWith("/api/")) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado");
			}
			else {
				response.sendRedirect("/login");
			}
		}))
		.logout(logout -> logout.permitAll())
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
}
