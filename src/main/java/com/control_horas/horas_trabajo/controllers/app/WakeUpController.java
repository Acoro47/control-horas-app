package com.control_horas.horas_trabajo.controllers.app;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class WakeUpController {
		
	@GetMapping("/wakeup")
	public ResponseEntity<Map<String, String>> wakeup(){
		
		Map<String, String> response = new HashMap<>();
		try {
			response.put("status", "ok");
		} catch (Exception e) {
			response.put("status", "stopped");
		}
		
		response.put("timestamp", Instant.now().toString());
		return ResponseEntity.ok(response);
		
	}

}
