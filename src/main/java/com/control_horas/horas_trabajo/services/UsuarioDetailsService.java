package com.control_horas.horas_trabajo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.securities.UsuarioPrincipal;


@Service
public class UsuarioDetailsService implements UserDetailsService {
	
	private final UsuarioRepository repo;
	
	public UsuarioDetailsService(UsuarioRepository uRepo) {
		this.repo = uRepo;
	}
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> userOpt = repo.findByUsername(username);
		
		if (userOpt.isEmpty()) throw new UsernameNotFoundException("Usuario no encontrado " + username);
		
		return new UsuarioPrincipal(userOpt.get());
		
	}
	
	public List<Usuario> obtenerTodosUsuarios(){
		return repo.findAll();
	}
	
	public Usuario obtenerUsuario(Long id) {
		return repo.findUserById(id);
	}
	
	public void actualizarUsuario(Usuario u) {
		repo.save(u);
	}
	
	public boolean existePorNombreOEmail(String username, String email) {
		return repo.existsByUsername(username) || repo.existsByMail(email);
	}
	
	
	

}
