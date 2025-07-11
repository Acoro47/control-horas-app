package com.control_horas.horas_trabajo.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.services.RegistroService;

@SpringBootTest
public class InyeccionRegistroTest {

	
	
	@Autowired
	private RegistroService regService;
	
	@Autowired
	private UsuarioRepository uRepo;
	
	@Autowired
	private RegistroRepository rRepo;
	
	@Test
	public void generarRegistrosJunioParaUsuario() {
		Usuario user = uRepo.findById(3L).orElseThrow();
		
		//regService.inyectarRegistrosJunioAvanzado(user.getId());
		
		long total = rRepo.findByUsuarioId(user.getId()).stream()
				.filter(r -> r.getHoraEntrada().getMonthValue() == 5 && r.getHoraEntrada().getYear() == 2025)
				.count();
		
		System.out.println("📋 Total de registros en junio 2025: " + total);
	}
	
	

}
