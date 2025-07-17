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
	
}
