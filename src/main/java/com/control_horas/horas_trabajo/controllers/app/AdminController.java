package com.control_horas.horas_trabajo.controllers.app;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.app.AuditLogDTO;
import com.control_horas.horas_trabajo.services.AuditLogService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
	
	private AuditLogService logService;

	public AdminController(AuditLogService logService) {
		
		this.logService = logService;
	}
	
	@GetMapping("/entity/{entityName}/{entityId}")
	public List<AuditLogDTO> getLogsByEntity(@PathVariable String entityName,
			@PathVariable Long entityId) {
		return logService.getLogsByEntity(entityName, entityId);
	}

}
