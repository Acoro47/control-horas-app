package com.control_horas.horas_trabajo.dtos.app;

public class ErrorResponse {
	
	private String message;
	
	public ErrorResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

}
