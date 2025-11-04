package com.control_horas.horas_trabajo.dtos.app;

public class EstadisticaDTO {
	
	private Long activos;
	private Long desactivados;
	private Long bloqueados;
	private Long admins;
	private Long users;
	
	public EstadisticaDTO(Long activos, Long desactivados, Long bloqueados, Long admins, Long users) {
		super();
		this.activos = activos;
		this.desactivados = desactivados;
		this.bloqueados = bloqueados;
		this.admins = admins;
		this.users = users;
	}

	public EstadisticaDTO() {
		super();
	}

	public Long getActivos() {
		return activos;
	}

	public void setActivos(Long activos) {
		this.activos = activos;
	}

	public Long getDesactivados() {
		return desactivados;
	}

	public void setDesactivados(Long desactivados) {
		this.desactivados = desactivados;
	}

	public Long getBloqueados() {
		return bloqueados;
	}

	public void setBloqueados(Long bloqueados) {
		this.bloqueados = bloqueados;
	}

	public Long getAdmins() {
		return admins;
	}

	public void setAdmins(Long admins) {
		this.admins = admins;
	}

	public Long getUsers() {
		return users;
	}

	public void setUsers(Long users) {
		this.users = users;
	}
	
	
	

}
