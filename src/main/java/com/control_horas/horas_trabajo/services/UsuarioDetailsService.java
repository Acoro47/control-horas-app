package com.control_horas.horas_trabajo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.dtos.app.EstadisticaDTO;
import com.control_horas.horas_trabajo.dtos.app.UsuarioCompletoDTO;
import com.control_horas.horas_trabajo.entities.Role;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.securities.UsuarioPrincipal;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    
    public UsuarioDetailsService(UsuarioRepository uRepo, PasswordEncoder enco) {
        this.repo = uRepo;
        this.encoder = enco;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new UsuarioPrincipal(usuario);
    }
    
    public List<Usuario> obtenerTodosUsuarios(){
        return repo.findAll();
    }
    
    public Optional<Usuario> obtenerUsuario(Long id) {
        return repo.findById(id);
    }
    
    public void actualizarUsuario(Usuario u) {
        repo.save(u);
    }
    
    public boolean existePorNombreOEmail(String username, String email) {
        return repo.existsByUsername(username) || repo.existsByMail(email);
    }
    
    // Comprobar usuario en el login de la app
    public boolean validarCredenciales(String username, String password) {
        return repo.findByUsername(username)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }
    
    public Usuario obtenerUsuarioPorNombre(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
    
    /* Métodos añadidos para la pantalla de administración */
    public List<UsuarioCompletoDTO> listarUsuarios(){
        return repo.findAllByOrderByIdAsc().stream()
                .map(UsuarioCompletoDTO::fromEntity)
                .toList();
    }
    
    public void alternarBloqueo(Long id) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        u.setAccountNonLocked(!u.isAccountNonLocked());
        repo.save(u);
    }
    
    public void alternarActivo(Long id) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        u.setEnabled(!u.isEnabled());
        repo.save(u);
    }
    
    public EstadisticaDTO obtenerEstadisticas() {
        Long activos = repo.countByEnabledTrue();
        Long desactivados = repo.countByEnabledFalse();
        
        Long desbloqueados = repo.countByAccountNonLockedTrue();
        Long bloqueados = repo.countByAccountNonLockedFalse();
        
        Long admins = repo.countByRol(Role.ADMIN);
        Long users = repo.countByRol(Role.USER);
        
        return new EstadisticaDTO(activos, desactivados, bloqueados, admins, users);
    }
}
