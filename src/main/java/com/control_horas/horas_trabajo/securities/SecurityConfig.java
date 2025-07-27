package com.control_horas.horas_trabajo.securities;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.control_horas.horas_trabajo.components.CustomSuccessHandler;
import com.control_horas.horas_trabajo.logger.LoggingAuthenticationFilter;



@Configuration
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final CustomSuccessHandler handler;
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, CustomSuccessHandler handler) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.handler = handler;
	}
	
	@Bean 
	@Order(1) 
	public SecurityFilterChain webFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
		LoggingAuthenticationFilter loginFilter = new LoggingAuthenticationFilter(authManager); 
		loginFilter.setFilterProcessesUrl("/login");
		loginFilter.setAuthenticationSuccessHandler(handler);
		http 
		.csrf(csrf -> csrf.disable()) 
		.authorizeHttpRequests(auth -> auth .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/guardarUsuario").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN") .anyRequest().authenticated() ) 
		.addFilter(loginFilter) .logout(logout -> logout .logoutUrl("/logout") 
				.logoutSuccessUrl("/login?logout") 
				.permitAll() 
				); 
		return http.build(); }
		
	@Bean
	@Order(2) 
	public SecurityFilterChain apifilterChain(HttpSecurity http) throws Exception {
		http 
		.securityMatcher("/api/**")
		.csrf(csrf -> csrf.disable()) 
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
		.authorizeHttpRequests(auth -> auth .requestMatchers("/api/login", "/api/enviarToken").permitAll() 
				.anyRequest().authenticated() ) .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build(); 
		} 
	
	
}
