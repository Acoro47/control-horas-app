package com.abel.control_horas_app.control_horas_app.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;

@Entity
public class Jornada {
	
	private int id;
	private LocalDate fecha;
	// Many to One
	private Usuario usuario;

}
