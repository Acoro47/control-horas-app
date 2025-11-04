package com.control_horas.horas_trabajo.dtos.app;

public sealed interface LoginResponse
	permits TokenResponse, LoginErrorResponse {}
