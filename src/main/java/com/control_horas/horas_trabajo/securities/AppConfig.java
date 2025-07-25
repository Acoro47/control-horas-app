package com.control_horas.horas_trabajo.securities;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AppConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
		throws Exception {
			return config.getAuthenticationManager();
	}
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(System.getenv("MAIL_HOST"));
		mailSender.setPort(Integer.parseInt(System.getenv("MAIL_PORT")));
		mailSender.setUsername(System.getenv("MAIL_USERNAME"));
		mailSender.setPassword(System.getenv("MAIL_PASSWORD"));
		
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol","smtp");
		props.put("mail.smtp.auth","true");
		props.put("mail.smtp.starttls.enable","true");
		
		return mailSender;
		
	}

}
