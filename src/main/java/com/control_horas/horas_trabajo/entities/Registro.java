package com.control_horas.horas_trabajo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="registro")
public class Registro {
	
	
	
	public Registro() {
		
	}

	public Registro(Usuario usuario,LocalDateTime horaEntrada, LocalDateTime horaSalida) {
		this();
		this.horaEntrada = horaEntrada;
		this.horaSalida = horaSalida;
		this.usuario = usuario;
	}

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime horaEntrada;
	private LocalDateTime horaSalida;
	
	@ManyToOne
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(LocalDateTime entrada) {
		this.horaEntrada = entrada;
	}

	public LocalDateTime getHoraSalida() {
		return horaSalida;
	}

	public void setHoraSalida(LocalDateTime salida) {
		this.horaSalida = salida;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	

}
