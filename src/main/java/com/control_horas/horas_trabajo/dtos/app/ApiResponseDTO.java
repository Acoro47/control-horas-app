package com.control_horas.horas_trabajo.dtos.app;

public class ApiResponseDTO {

	private String  status;
	private String message;

	public ApiResponseDTO() {

	}

	public ApiResponseDTO(String status, String mes) {
		this();
		this.status = status;
		this.message = mes;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}



}
