package com.control_horas.horas_trabajo.dtos.app;

public class UsuarioDTO {
	
	private Long id;
	private String username;
	private String mensaje;
	
	public UsuarioDTO(Long id, String name, String message) {
		this.id = id;
		this.username = name;
		this.mensaje = message;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
}
