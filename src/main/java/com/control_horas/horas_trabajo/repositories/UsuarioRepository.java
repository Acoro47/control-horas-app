package com.control_horas.horas_trabajo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.control_horas.horas_trabajo.entities.Role;
import com.control_horas.horas_trabajo.entities.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Optional<Usuario> findByUsername(String username);
	List<Usuario> findAllByOrderByIdAsc();
	boolean existsByUsername(String username);
	boolean existsByMail(String mail);
	Long countByEnabledTrue();
	Long countByEnabledFalse();
	Long countByAccountNonLockedFalse();
	Long countByAccountNonLockedTrue();
	Long countByRol(Role Role);
	
}


