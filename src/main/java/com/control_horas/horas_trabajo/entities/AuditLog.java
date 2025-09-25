package com.control_horas.horas_trabajo.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_log")
public class AuditLog {
	
	@Id 
	@GeneratedValue
	private Long id;
	
	private String entityName;
	private Long entityId;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private String changedBy;
	
	@CreationTimestamp
	private LocalDateTime changedAt;

	public AuditLog(Long id, String entityName, Long entityId, String fieldName, String oldValue, String newValue,
			String changedBy, LocalDateTime changedAt) {
		super();
		this.id = id;
		this.entityName = entityName;
		this.entityId = entityId;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.changedBy = changedBy;
		this.changedAt = changedAt;
	}

	public AuditLog() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getChangedBy() {
		return changedBy;
	}

	public void setChangedBy(String changedBy) {
		this.changedBy = changedBy;
	}

	public LocalDateTime getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(LocalDateTime changedAt) {
		this.changedAt = changedAt;
	}
	
	

}
