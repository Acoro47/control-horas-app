package com.control_horas.horas_trabajo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenControllerApp {
	
	@GetMapping("/enviarToken")
	public ResponseEntity<Void> enviarToken(
			@RequestParam String username,
			@RequestParam String email
			){
		
		return ResponseEntity.ok().build();
		
	}

}
