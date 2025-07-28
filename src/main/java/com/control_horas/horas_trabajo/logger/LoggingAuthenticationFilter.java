package com.control_horas.horas_trabajo.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoggingAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAuthenticationFilter.class);

	public LoggingAuthenticationFilter(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
	
	@Override
	public org.springframework.security.core.Authentication attemptAuthentication(
			HttpServletRequest request,
			HttpServletResponse response) {
		
		logger.info("[LOGIN] Se está procesando autenticación para usuario: {}", request.getParameter("username"));
		
		return super.attemptAuthentication(request, response);
		
	}
}
