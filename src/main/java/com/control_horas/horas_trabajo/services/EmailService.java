package com.control_horas.horas_trabajo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.controllers.web.SolicitudController;
import com.control_horas.horas_trabajo.entities.SolicitudAcceso;

@Service
public class EmailService {
	
	private final Logger logger = LoggerFactory.getLogger(SolicitudController.class);
	
	@Autowired
	private JavaMailSender sender;
	
	public void enviarAprobacion(SolicitudAcceso solicitud) {
		
		logger.info("Función para enviar aprobación" + solicitud.toString());
		
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(solicitud.getEmail());
		mensaje.setSubject("Solicitud aprobada - Activación de cuenta");
		mensaje.setText("Hola " + solicitud.getUsername() + ",\n\n"
		        + "Tu solicitud de acceso ha sido aprobada.\n"
		        + "Para activar tu cuenta, haz clic en el siguiente enlace:\n"
		        + "https://control-horas-app-1.onrender.com/activar?token=" + solicitud.getToken() + "\n\n"
		        + "Si no esperabas este correo, puedes ignorarlo.\n\n"
		        + "¡Bienvenido!");
		    sender.send(mensaje);
	}
	
	public void enviarRechazo(SolicitudAcceso solicitud) {
		logger.info("Función para enviar rechazo" + solicitud.toString());
	    SimpleMailMessage mensaje = new SimpleMailMessage();
	    mensaje.setTo(solicitud.getEmail());
	    mensaje.setSubject("Solicitud rechazada");
	    mensaje.setText("Hola " + solicitud.getUsername() + ",\n\n"
	        + "Tu solicitud para acceder al sistema ha sido revisada, pero no ha sido aprobada en este momento.\n\n"
	        + "Si crees que se trata de un error, puedes contactar con el administrador en admin@control-horas.com.\n\n"
	        + "Gracias por tu interés."
	        + "Saludos."
	    		);
	    sender.send(mensaje);
	}
	

}
