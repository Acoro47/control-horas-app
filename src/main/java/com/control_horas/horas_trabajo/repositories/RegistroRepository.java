package com.control_horas.horas_trabajo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;


public interface RegistroRepository extends JpaRepository<Registro, Long>{
	
	Optional<Registro> findFirstByUsuarioAndHoraSalidaIsNullOrderByHoraEntrada(Usuario usuario);
	
	@Query("SELECT r FROM Registro r WHERE r.usuario_id = :id ORDER BY r.id ASC" )
	List<Registro> findByUsuarioId(@Param("id") Long id);

}
