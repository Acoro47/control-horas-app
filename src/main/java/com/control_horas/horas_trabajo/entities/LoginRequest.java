package com.control_horas.horas_trabajo.entities;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
	
	@NotBlank(message = "El usuario es obligatorio")
	private String username;
	
	@NotBlank(message = "La contraseña es obligatoria")
	private String password;
	
	
	public LoginRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
