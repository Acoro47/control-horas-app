package com.control_horas.horas_trabajo.dtos.app;

public record LoginErrorResponse(
		String message, 
		String status
		) implements LoginResponse {

}
