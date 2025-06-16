package com.abel.control_horas_app.control_horas_app.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name="usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false,unique=true)
	private String email;
	
	@Column(nullable=false)
	private boolean activo = true;
	
	// Encriptada
	@Column(nullable=false)
	private String password;
	
	@ManyToMany(fetch= FetchType.EAGER)
	@JoinTable(
			name="usuarios_roles",
			joinColumns = @JoinColumn(name="rol_id"),
			inverseJoinColumns = @JoinColumn(name = "rol_id")
			)
	
	
	private Rol rol;

}
