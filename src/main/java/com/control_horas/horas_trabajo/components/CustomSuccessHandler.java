package com.control_horas.horas_trabajo.components;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
										HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		String redirectionURL = "/";
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			
			if(role.equals("ROLE_ADMIN")) {
				System.err.println("Administrador");
				redirectionURL = "/admin";
				System.err.println("Redireccionando a " + redirectionURL);
				break;
			} 
			else if(role.equals("ROLE_USER")){
				System.err.println("Usuario");
				redirectionURL = "/panel";
				System.err.println("Redireccionando a " + redirectionURL);
				break;
			}
		}
		
		logger.info("Login exitoso para usuario: {}", authentication.getName());
		
		response.sendRedirect(redirectionURL);
		
	}

}
