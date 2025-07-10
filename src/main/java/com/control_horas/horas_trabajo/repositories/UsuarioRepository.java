package com.control_horas.horas_trabajo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.control_horas.horas_trabajo.entities.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByUsername(String username);
	Usuario findUserById(Long id);
}


