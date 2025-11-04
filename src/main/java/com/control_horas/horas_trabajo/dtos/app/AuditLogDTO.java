package com.control_horas.horas_trabajo.dtos.app;

public class AuditLogDTO {
	private String entityName;
	private Long entityId;
	private String fieldName;
	private String oldValue;
	private String newValue;
	private String changedBy;
	private String changedAt;

	public AuditLogDTO(String entityName, Long entityId, String fieldName, String oldValue, String newValue,
			String changedBy, String changedAt) {
		super();
		this.entityName = entityName;
		this.entityId = entityId;
		this.fieldName = fieldName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.changedBy = changedBy;
		this.changedAt = changedAt;
	}

	public AuditLogDTO() {
		super();
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

	public String getChangedAt() {
		return changedAt;
	}

	public void setChangedAt(String changedAt) {
		this.changedAt = changedAt;
	}



}
