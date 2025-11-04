package com.control_horas.horas_trabajo.controllers.app;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.dtos.app.EstadisticaDTO;
import com.control_horas.horas_trabajo.dtos.app.UsuarioCompletoDTO;
import com.control_horas.horas_trabajo.services.RegistroService;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AppAdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AppAdminController.class);
    private final UsuarioDetailsService service;
    private final RegistroService regService;

    public AppAdminController(UsuarioDetailsService service, RegistroService regService) {
        this.service = service;
        this.regService = regService;
    }
    
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioCompletoDTO>> obtenerUsuarios() {
        List<UsuarioCompletoDTO> usuarios = service.listarUsuarios();
        if (!usuarios.isEmpty()) {
            logger.info("Lista de usuarios backend: Username: {}, Rol: {}", usuarios.get(0).getUsername(), usuarios.get(0).getRol());
        } else {
            logger.info("Lista de usuarios backend: vacía");
        }
        return ResponseEntity.ok(usuarios);
    }
    
    @PostMapping("/toggle-lock/{id}")
    public ResponseEntity<Void> alternarBloqueo(@PathVariable Long id) {
        service.alternarBloqueo(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/toggle-enabled/{id}")
    public ResponseEntity<Void> alternarActivo(@PathVariable Long id) {
        service.alternarActivo(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/estadisticas")
    public ResponseEntity<EstadisticaDTO> obtenerEstadisticas(){
        EstadisticaDTO dto = service.obtenerEstadisticas();
        return ResponseEntity.ok(dto);
    }
}
