package com.control_horas.horas_trabajo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.entities.SolicitudAcceso;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender sender;
	
	public void enviarAprobacion(SolicitudAcceso solicitud) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setTo(solicitud.getEmail());
		mensaje.setSubject("Solicitud aprobada - Activación de cuenta");
		mensaje.setText("Hola " + solicitud.getUsername() + ",\n\n"
		        + "Tu solicitud de acceso ha sido aprobada.\n"
		        + "Para activar tu cuenta, haz clic en el siguiente enlace:\n"
		        + "https://control-horas-app-production.up.railway.app/activar?token=" + solicitud.getToken() + "\n\n"
		        + "Si no esperabas este correo, puedes ignorarlo.\n\n"
		        + "¡Bienvenido!");
		    sender.send(mensaje);
	}
	
	public void enviarRechazo(SolicitudAcceso solicitud) {
	    SimpleMailMessage mensaje = new SimpleMailMessage();
	    mensaje.setTo(solicitud.getEmail());
	    mensaje.setSubject("Solicitud rechazada");
	    mensaje.setText("Hola " + solicitud.getUsername() + ",\n\n"
	        + "Tu solicitud para acceder al sistema ha sido revisada, pero no ha sido aprobada en este momento.\n\n"
	        + "Si crees que se trata de un error, puedes contactar con el administrador.\n\n"
	        + "Gracias por tu interés.");
	    sender.send(mensaje);
	}
	

}
