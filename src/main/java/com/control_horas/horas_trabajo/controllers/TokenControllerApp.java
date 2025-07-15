package com.control_horas.horas_trabajo.controllers;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenControllerApp {
	
	@GetMapping(value = "/enviarToken", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String enviarToken(
			@RequestParam String username,
			@RequestParam String email
			){
		
		return "✅ Token enviado correctamente";
		
	}

}
