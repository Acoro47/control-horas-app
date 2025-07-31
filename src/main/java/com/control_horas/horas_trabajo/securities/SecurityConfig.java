package com.control_horas.horas_trabajo.securities;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.control_horas.horas_trabajo.components.CustomSuccessHandler;



@Configuration
public class SecurityConfig {
		
	private CustomSuccessHandler handler;	
	private final JwtAuthenticationFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthenticationFilter filter, CustomSuccessHandler handler) {
		this.handler = handler;
		this.jwtAuthFilter = filter;
	}
	
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring()
	        // Ignora todos los recursos estáticos en /static, /public, /resources, /META-INF/resources…
	        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
	        // Si tienes adicionalmente imágenes, fuentes, etc.
	        .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**");
	}
		
	@Bean
	@Order(1) 
	public SecurityFilterChain apifilterChain(HttpSecurity http) throws Exception {
		http 
		.securityMatcher("/api/**")
		.csrf(csrf -> csrf.disable()) 
		.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.exceptionHandling(ex -> ex
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				)
		.authorizeHttpRequests(auth -> auth 
				.requestMatchers("/api/login", "/api/enviarToken").permitAll() 
				.anyRequest().authenticated() 
				)
		
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build(); 
		}
	
	@Bean 
	@Order(2) 
	public SecurityFilterChain webFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
		
		http
		.securityMatcher(request -> {
				String path = request.getRequestURI();
				return !path.startsWith("/api/");
		})
		.authorizeHttpRequests(auth -> auth 
				.requestMatchers(
						"/login",
						"/solicitar",
						"/activar",
						"/registro",
						"/guardarUsuario"
						).permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				)
		.formLogin(form -> form
				.loginPage("/login")
				.permitAll()
				.successHandler(handler)
				.failureUrl("/login?error")
				)
		.logout(logout -> logout .logoutUrl("/logout") 
				.logoutSuccessUrl("/login?logout") 
				.permitAll() 
				);
		
		return http.build(); 
		
	}	
}
