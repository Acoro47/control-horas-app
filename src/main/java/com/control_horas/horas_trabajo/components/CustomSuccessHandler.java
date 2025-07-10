package com.control_horas.horas_trabajo.components;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, 
										HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		String redirectionURL = "/login";
		for (GrantedAuthority authority : authorities) {
			String role = authority.getAuthority();
			
			if(role.equals("ROLE_ADMIN")) {
				System.err.println("Administrador");
				redirectionURL = "/admin/panel";
				break;
			} 
			else if(role.equals("ROLE_USER")){
				System.err.println("Usuario");
				redirectionURL = "/panel";
				break;
			}
		}
		
		response.sendRedirect(redirectionURL);
		
	}

}
