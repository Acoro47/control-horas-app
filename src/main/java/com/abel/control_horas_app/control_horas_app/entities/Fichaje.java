package com.abel.control_horas_app.control_horas_app.entities;

import java.time.LocalTime;

import jakarta.persistence.Entity;

@Entity
public class Fichaje {
	
	private int id;
	private Tipo tipo;
	private LocalTime hora;
	// Many to One
	private Jornada jornada;

}
