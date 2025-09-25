package com.control_horas.horas_trabajo.services;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.dtos.app.AuditLogDTO;
import com.control_horas.horas_trabajo.entities.AuditLog;
import com.control_horas.horas_trabajo.repositories.AuditLogRepository;

@Service
public class AuditLogService {
	
	private AuditLogRepository repository;
	
	public AuditLogService(AuditLogRepository repo) {
		this.repository = repo;
	}
	
	public void logChange(String entityName, Long entityId, String fieldName, String oldValue,
			String newValue, String changedBy) {
		AuditLog log = new AuditLog();
		
		log.setEntityName(entityName);
		log.setEntityId(entityId);
		log.setFieldName(fieldName);
		log.setOldValue(oldValue);
		log.setNewValue(newValue);
		log.setChangedBy(changedBy);
		repository.save(log);
	}
	
	public List<AuditLogDTO> getLogsByEntity(String entityName, Long entityId) {
		return repository.findByEntityNameAndEntityIdOrderByChangedAtDesc(entityName, entityId)
				.stream()
				.map(this::toDto)
				.collect(Collectors.toList());
	}
	
	public AuditLogDTO toDto(AuditLog log) {
		AuditLogDTO dto = new AuditLogDTO();
	    dto.setEntityName(log.getEntityName());
	    dto.setEntityId(log.getEntityId());
	    dto.setFieldName(log.getFieldName());
	    dto.setOldValue(log.getOldValue());
	    dto.setNewValue(log.getNewValue());
	    dto.setChangedBy(log.getChangedBy());
	    dto.setChangedAt(log.getChangedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	    return dto;
	}

}
