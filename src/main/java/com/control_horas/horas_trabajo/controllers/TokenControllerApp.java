package com.control_horas.horas_trabajo.controllers;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenControllerApp {
	
	@GetMapping("/enviarToken")
	public ResponseEntity<String> enviarToken(
			@RequestParam String username,
			@RequestParam String email
			){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		
		return new ResponseEntity<>("✅ Token enviado correctamente a " + email, headers, HttpStatus.OK);
		
	}

}
