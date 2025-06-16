package com.abel.control_horas_app.control_horas_app.entities;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="fichajes")
public class Fichaje {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false)
	private Tipo tipo;
	
	@Column(nullable=false)
	private LocalTime hora;
	
	// Many to One
	private Jornada jornada;

}
