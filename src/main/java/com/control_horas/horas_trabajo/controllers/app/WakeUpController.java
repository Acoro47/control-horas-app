package com.control_horas.horas_trabajo.controllers.app;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.services.ServerStatusService;

@RestController
@RequestMapping("/api")
public class WakeUpController {
	
	private final ServerStatusService serverStatus;
	
	public WakeUpController(ServerStatusService service) {
		this.serverStatus = service;
	}
	
	@GetMapping("/wakeup")
	public ResponseEntity<Map<String, String>> wakeup(){
		
		Map<String, String> response = new HashMap<>();
		boolean serverRunning = serverStatus.checkServerStatus();
		
		response.put("status", serverRunning ? "running" : "stopped");
		response.put("timestamp", Instant.now().toString());
		return ResponseEntity.ok(response);
		
	}

}
