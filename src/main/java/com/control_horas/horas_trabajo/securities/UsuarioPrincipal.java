package com.control_horas.horas_trabajo.securities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.control_horas.horas_trabajo.entities.Usuario;

public class UsuarioPrincipal implements UserDetails{
	
	private Usuario usuario;
	
	public UsuarioPrincipal(Usuario u) {
		this.usuario = u;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
	}

	@Override
	public String getPassword() {
		
		return usuario.getPassword();
	}

	@Override
	public String getUsername() {
		// 
		return usuario.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return usuario.isAccountNonExpired();
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return usuario.isCredentialsNonExpired();
	}
	
	@Override
	public boolean isEnabled() {
		return usuario.isEnabled();
	}
	
	@Override
	public boolean isAccountNonLocked() {
	    return usuario.isAccountNonLocked();
	}

}
