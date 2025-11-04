package com.control_horas.horas_trabajo.controllers.app;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.control_horas.horas_trabajo.services.RegistroService;
import com.control_horas.horas_trabajo.services.UsuarioDetailsService;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AppAdminController {

	private final RegistroService regService;
	private final UsuarioDetailsService userService;

	public AppAdminController(UsuarioDetailsService uServ,RegistroService serv) {
		this.regService = serv;
		this.userService = uServ;
	}




}
