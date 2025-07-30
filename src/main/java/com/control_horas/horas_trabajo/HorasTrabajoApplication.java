package com.control_horas.horas_trabajo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;



@SpringBootApplication(exclude = MailSenderAutoConfiguration.class)
public class HorasTrabajoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HorasTrabajoApplication.class, args);
	}
	
	
	/*
	@Bean
    CommandLineRunner cargarRegistrosSimulados(RegistroRepository registroRepo, UsuarioRepository usuarioRepo) {
        return args -> {
            // Buscamos usuario de prueba
            Optional<Usuario> usuarioOpt = usuarioRepo.findByUsername("raquel");
            if (usuarioOpt.isEmpty()) {
                System.out.println("⚠️ Usuario 'prueba' no encontrado. No se generan registros.");
                return;
            }

            Usuario usuario = usuarioOpt.get();

            List<Registro> registros = new ArrayList<>();
            
            

            for (int dia = 1; dia <= 30; dia++) {
                LocalDate fecha = LocalDate.of(2025, 7, dia);
                DayOfWeek diaSemana = fecha.getDayOfWeek();

                if (diaSemana == DayOfWeek.WEDNESDAY) continue;

                if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
                	if (dia == 12) {
                		registros.add(new Registro(
                                usuario,
                                fecha.atTime(7, 30),
                                fecha.atTime(14, 00)
                            ));
                	}
                	else if(dia == 20) {
                		registros.add(new Registro(
                                usuario,
                                fecha.atTime(7, 30),
                                fecha.atTime(14, 30)
                            ));
                	}
                	else if(dia == 27) {
                		registros.add(new Registro(
                                usuario,
                                fecha.atTime(7, 30),
                                fecha.atTime(15, 15)
                            ));
                	}
                	else {
                		registros.add(new Registro(
                                usuario,
                                fecha.atTime(7, 30),
                                fecha.atTime(15, 00)
                            ));
                	}
                    
                } else {
                	
                	if (dia == 1 || 
            			dia == 7 ||
            			dia == 8 ||
            			dia == 10 ||
            			dia == 11 ||
            			dia == 14 ||
            			dia == 28 ) {
                		
                		registros.add(new Registro(
                                usuario,
                                fecha.atTime(7, 30),
                                fecha.atTime(12, 30)
                            ));
                            registros.add(new Registro(
                                usuario,
                                fecha.atTime(17, 00),
                                fecha.atTime(21, 30)
                            ));
                		
                	}  else if (dia == 4 || 
                			dia == 15 ||
                			dia == 17 ||
                			dia == 21 ||
                			dia == 24) {
                	
	                    registros.add(new Registro(
	                        usuario,
	                        fecha.atTime(7, 30),
	                        fecha.atTime(12, 30)
	                    ));
	                    registros.add(new Registro(
	                        usuario,
	                        fecha.atTime(17, 00),
	                        fecha.atTime(21, 45)
                		));
	                    
                	} else if (dia == 3 || 
                			dia == 18 ||
                			dia == 8 ||
                			dia == 22 ||
                			dia == 25) {	
                	
	                    registros.add(new Registro(
	                        usuario,
	                        fecha.atTime(7, 30),
	                        fecha.atTime(12, 30)
	                    ));
	                    registros.add(new Registro(
	                        usuario,
	                        fecha.atTime(17, 00),
	                        fecha.atTime(22, 00)
	                    ));
                	} else {
                		registros.add(new Registro(
    	                        usuario,
    	                        fecha.atTime(7, 30),
    	                        fecha.atTime(12, 30)
    	                    ));
    	                    registros.add(new Registro(
    	                        usuario,
    	                        fecha.atTime(17, 00),
    	                        fecha.atTime(21, 45)
                    		));
            		}
                		
            	}	
                	
            }

            registroRepo.saveAll(registros);
            System.out.println("✅ Registros simulados cargados para junio.");
        };
    }  
	
	*/

}
