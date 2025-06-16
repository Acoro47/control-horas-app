package com.abel.control_horas_app.control_horas_app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Rol {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY ) 
	private int id;
	
	@Column
	private String Admin;
	
	@Column
	private Usuario User;

}
