package com.control_horas.horas_trabajo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;


public interface RegistroRepository extends JpaRepository<Registro, Long>{
	
	Optional<Registro> findFirstByUsuarioAndHoraSalidaIsNullOrderByHoraEntrada(Usuario usuario);

}
