package com.control_horas.horas_trabajo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	public void enviarToken(String destinatario, String nombre, String token) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(destinatario);
		mensaje.setSubject("Token acceso");
		mensaje.setText("Hola " + nombre + ",\n\n"
	            + "Tu token de activación es:\n"
	            + "http://localhost:8080/activar?token=" + token + "\n\n"
	            + "¡Bienvenido!");
		sender.send(mensaje);
	}

}
