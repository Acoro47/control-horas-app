package com.control_horas.horas_trabajo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SolicitudAcceso {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	private String username;
	
	private EstadoSolicitud estado;
	
	private String token;
	
	private LocalDateTime fechaSolicitud;
	private LocalDateTime fechaAprobacion;
	
	public SolicitudAcceso() {
		
	}

	public SolicitudAcceso(String email, String username, EstadoSolicitud estado,
			LocalDateTime fechaSolicitud) {
		this();
		this.email = email;
		this.username = username;
		this.estado = estado;
		this.token = token;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaAprobacion = fechaAprobacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public EstadoSolicitud getEstado() {
		return estado;
	}

	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDateTime getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	public LocalDateTime getFechaAprobacion() {
		return fechaAprobacion;
	}

	public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	
	
	
	

}
