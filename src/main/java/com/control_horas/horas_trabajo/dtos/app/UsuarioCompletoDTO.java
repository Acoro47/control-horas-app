package com.control_horas.horas_trabajo.dtos.app;

import com.control_horas.horas_trabajo.entities.Role;
import com.control_horas.horas_trabajo.entities.Usuario;

public class UsuarioCompletoDTO {
	
	private Long id;
	private String username;
	private String email;
	private Role rol;
	private Boolean enabled;
	private Boolean accountNonLocked;
	
	public UsuarioCompletoDTO() {
		
	}
	
	public UsuarioCompletoDTO(Long id, String username, String email, Role rol, Boolean enabled,
			Boolean accountNonLocked) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.rol = rol;
		this.enabled = enabled;
		this.accountNonLocked = accountNonLocked;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRol() {
		return rol;
	}

	public void setRol(Role rol) {
		this.rol = rol;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	
	
	public static UsuarioCompletoDTO fromEntity(Usuario usuario) {
		UsuarioCompletoDTO dto = new UsuarioCompletoDTO();
		dto.setId(usuario.getId());
		dto.setUsername(usuario.getUsername());
		dto.setEmail(usuario.getMail());
		dto.setAccountNonLocked(usuario.isAccountNonLocked());
		dto.setEnabled(usuario.isEnabled());
		dto.setRol(usuario.getRol());
		return dto;
		
	}
	
	

}
