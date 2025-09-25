package com.control_horas.horas_trabajo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.control_horas.horas_trabajo.entities.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>{
	
	List<AuditLog> findByEntityNameAndEntityIdOrderByChangedAtDesc(String entityName, Long entityId);
	List<AuditLog> findByChangedByOrderByChangedAtDesc(String changedBy);

}
