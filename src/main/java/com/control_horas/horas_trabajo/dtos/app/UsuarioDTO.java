package com.control_horas.horas_trabajo.dtos.app;

import java.util.List;

import com.control_horas.horas_trabajo.entities.Registro;

public final class UsuarioDTO {

	private Long id;
	private String username;
	private String mensaje;
	private String token;
	private List<Registro> registros;


	public UsuarioDTO(Long id, String name, String message, String tok, List<Registro> regs) {
		this.id = id;
		this.username = name;
		this.mensaje = message;
		this.token = tok;
		this.registros = regs;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Registro> getRegistros() {
		return registros;
	}

	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}





}
