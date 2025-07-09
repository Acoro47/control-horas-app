package com.control_horas.horas_trabajo.services;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	
	public List<Usuario> obtenerTodosUsuarios(){
		return repo.findAll();
	}
	
	public void resetearPassword(Long id) {
		Usuario user = repo.findById(id).orElseThrow();
		String claveTemporal = generarClaveTemporal();
		String claveHash = new BCryptPasswordEncoder().encode(claveTemporal);
		user.setPassword(claveHash);
		repo.save(user);
	}
	
	public String generarClaveTemporal() {
		int longitud = 10;
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#%";
		StringBuilder clave = new StringBuilder();
		SecureRandom random = new SecureRandom();
		
		for (int i = 0; i < longitud; i ++) {
			int index = random.nextInt(caracteres.length());
			clave.append(caracteres.charAt(index));
		}
		
		return clave.toString();
	}

}
