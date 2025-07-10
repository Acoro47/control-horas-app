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
public class RedireccionPorRolHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		String destino = "/panel";
		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_ADMIN")) {
				destino = "/admin";
				break;
			}
		}
		
		response.sendRedirect(destino);
		
	}

}
