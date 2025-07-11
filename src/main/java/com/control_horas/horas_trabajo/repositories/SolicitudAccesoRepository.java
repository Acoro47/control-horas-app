package com.control_horas.horas_trabajo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.control_horas.horas_trabajo.entities.EstadoSolicitud;
import com.control_horas.horas_trabajo.entities.SolicitudAcceso;

public interface SolicitudAccesoRepository  extends JpaRepository<SolicitudAcceso, Long>{
	
	Optional<SolicitudAcceso> findByToken(String token);
	List<SolicitudAcceso> findByEstado(EstadoSolicitud estado);
	boolean existsByEmail(String email);

}
