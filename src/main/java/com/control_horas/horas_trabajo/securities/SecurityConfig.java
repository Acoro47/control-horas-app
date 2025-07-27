package com.control_horas.horas_trabajo.securities;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
public class SecurityConfig {
	
	/*private final JwtAuthenticationFilter jwtAuthFilter;
	
	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}
	*/
	
	
	
	@Bean
	public SecurityFilterChain webFilterChain(HttpSecurity http,
			AuthenticationManager authManager,
			DaoAuthenticationProvider authProvider) throws Exception {
			
		http
		.authenticationProvider(authProvider)
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login", "/registro", "/css/**", "/js/**", "/guardarUsuario","/panel").permitAll()
			    .anyRequest().authenticated()
				)
		.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/perform_login")
				.defaultSuccessUrl("/panel",true)
				.failureUrl("/login?error=true")
				.permitAll()
				)
		.logout(logout -> logout
			    .logoutUrl("/logout")
			    .logoutSuccessUrl("/login?logout")
			    .permitAll()
			);
		
		return http.build();				
	}
}
