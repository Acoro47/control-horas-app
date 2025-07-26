package com.control_horas.horas_trabajo.securities;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class SecurityConfig {
	
	
	
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
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.httpBasic(h -> h.disable())
		.formLogin(f -> f.disable())
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
