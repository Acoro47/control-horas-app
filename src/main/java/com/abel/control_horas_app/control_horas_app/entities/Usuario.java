package com.abel.control_horas_app.control_horas_app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
	
	@Id
	private int id;
	private String email;
	// Encriptada
	private String password;
	private Role rol;

}
