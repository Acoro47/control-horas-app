package com.control_horas.horas_trabajo.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;


public interface RegistroRepository extends JpaRepository<Registro, Long>{
	
	Optional<Registro> findFirstByUsuarioAndHoraSalidaIsNullOrderByHoraEntrada(Usuario usuario);
	
	List<Registro> findByUsuarioId(Long id);
	
	List<Registro> findByUsuarioIdOrderByIdAsc(Long id);
	
	List<Registro> findByUsuarioIdAndHoraEntradaBetweenAndHoraSalidaIsNotNull(
			Long usuarioId,
			LocalDateTime inicio,
			LocalDateTime fin
			);
	List<Registro> findByUsuarioIdAndHoraEntradaBetween(
			Long usuarioId,
			LocalDateTime inicio,
			LocalDateTime fin
			);
	
	boolean existsByUsuarioIdAndHoraSalidaIsNull(long usuarioId);

}
