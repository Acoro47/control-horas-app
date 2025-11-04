package com.control_horas.horas_trabajo.dtos.app;

public record TokenResponse(
		String id,
		String username,
		String rol,
		String token
		) implements LoginResponse {}

