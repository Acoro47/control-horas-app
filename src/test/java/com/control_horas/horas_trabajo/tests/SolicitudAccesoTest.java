package com.control_horas.horas_trabajo.tests;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.control_horas.horas_trabajo.entities.EstadoSolicitud;
import com.control_horas.horas_trabajo.entities.SolicitudAcceso;
import com.control_horas.horas_trabajo.repositories.SolicitudAccesoRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SolicitudAccesoTest {
	
	@Autowired
	private SolicitudAccesoRepository repo;
	
	@Test
	public void flujoBasicoSolicitud() {
		SolicitudAcceso s = new SolicitudAcceso();
		s.setEmail("usuario@example.com");
        s.setUsername("Abel Prueba");
        s.setEstado(EstadoSolicitud.PENDIENTE);
        s.setFechaSolicitud(LocalDateTime.now());
		repo.save(s);
		System.out.println("solicitud registrada..." + s.getId());
		
		s.setEstado(EstadoSolicitud.APROBADA);
		s.setToken(UUID.randomUUID().toString());
		s.setFechaAprobacion(LocalDateTime.now());
		repo.save(s);
		System.out.println("Solicitud aprobada con token: " + s.getToken());
		
		Optional<SolicitudAcceso> validacion = repo.findByToken(s.getToken());
		assertTrue(validacion.isPresent(), "Token válido encontrado");
		
		
	}
	
	@AfterEach
	void limpiar() {
		repo.deleteAll();
	}

}
