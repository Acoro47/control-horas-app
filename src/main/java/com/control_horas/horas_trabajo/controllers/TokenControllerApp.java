package com.control_horas.horas_trabajo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenControllerApp {
	
	@GetMapping(value = "/enviarToken", produces = "text/plain;charset=UTF-8")
	public ResponseEntity<Map<String,String>> enviarToken(
			@RequestParam String username,
			@RequestParam String email
			){
		
		Map<String, String> respuesta = new HashMap<>();
		respuesta.put("estado", "ok");
		respuesta.put("mensaje", "✅ Token enviado correctamente");
		
		return ResponseEntity.ok(respuesta);
		
	}

}
