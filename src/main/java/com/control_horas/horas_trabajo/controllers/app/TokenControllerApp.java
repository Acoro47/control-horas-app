package com.control_horas.horas_trabajo.controllers.app;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.ApiResponseDTO;

@RestController
@RequestMapping("/api")
public class TokenControllerApp {
	
	@GetMapping("/enviarToken")
	public ResponseEntity<ApiResponseDTO> enviarToken(
			@RequestParam String username,
			@RequestParam String email
			){
		
		ApiResponseDTO response = new ApiResponseDTO("success", "✅ Token enviado correctamente a " + email);
		return ResponseEntity.ok(response);
		
	}

}
