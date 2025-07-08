package com.control_horas.horas_trabajo.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;


@Service
public class UsuarioDetailsService implements UserDetailsService {
	
	private final UsuarioRepository repo;
	
	public UsuarioDetailsService(UsuarioRepository uRepo) {
		this.repo = uRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		
		return new User(user.getUsername(), user.getPassword(),
				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRol())));
		
		
	}

}
