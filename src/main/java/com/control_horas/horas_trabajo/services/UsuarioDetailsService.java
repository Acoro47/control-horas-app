package com.control_horas.horas_trabajo.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.securities.UsuarioPrincipal;


@Service
public class UsuarioDetailsService implements UserDetailsService {
	
	private final UsuarioRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public UsuarioDetailsService(UsuarioRepository uRepo) {
		this.repo = uRepo;
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		
		return new UsuarioPrincipal(user);
		
	}
	
	public List<Usuario> obtenerTodosUsuarios(){
		return repo.findAll();
	}
	
	public Usuario obtenerUsuario(Long id) {
		return repo.findUserById(id);
	}
	
	public void actualizarPassword(Long id, String newPass) {
		Usuario user = repo.findById(id).orElseThrow();
		String hash = passwordEncoder.encode(newPass);
		user.setPassword(hash);
		repo.save(user);
	}
	
	

}
