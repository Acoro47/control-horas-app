package com.control_horas.horas_trabajo.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;

@Service
public class RegistroService {
	
	private final RegistroRepository regRepo;
	
	public RegistroService(RegistroRepository repo,UsuarioRepository uRepo) {
		this.regRepo = repo;
	}
	
	public Map<LocalDate, Long> calcularTiempoDia(Long usuarioId, LocalDate desde, LocalDate hasta){
		List<Registro> registros = regRepo.findByUsuarioId(usuarioId).stream()
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate fecha = r.getHoraEntrada().toLocalDate();
					return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
				})
				.toList();
		
		Map<LocalDate, Long> minutosDia =  registros.stream()
				.collect(Collectors.groupingBy(
						r -> r.getHoraEntrada().toLocalDate(),
						Collectors.summingLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
						));
		
		System.out.println(minutosDia);
		
		return minutosDia;
	}
	
	public Map<YearMonth, Long> calcularTiempoMes(Long usuarioId, LocalDate desde, LocalDate hasta){
		Map<LocalDate, Long> dias = calcularTiempoDia(usuarioId, desde, hasta);
				
		Map<YearMonth, Long> minutosMes = dias.entrySet().stream()
				.collect(Collectors.groupingBy(
						e -> YearMonth.from(e.getKey()),
						Collectors.summingLong(Map.Entry::getValue)
						));
		
		System.out.println(minutosMes);
		
		return minutosMes;
	}
	
	public Map<Integer, Long> calcularTiempoAnio(Long usuarioId, LocalDate desde, LocalDate hasta){
		Map<LocalDate, Long> dias = calcularTiempoDia(usuarioId, desde, hasta);
				
		Map<Integer, Long> minutosAnio = dias.entrySet().stream()
				.collect(Collectors.groupingBy(
						e -> e.getKey().getYear(),
						Collectors.summingLong(Map.Entry::getValue)
						));
		
		System.out.println(minutosAnio);
		
		return minutosAnio;
	}
	
	public String formatoHoras(long minutos) {
		long horas = minutos / 60;
		long resto = minutos % 60;
		
		return String.format("%02d:%02d", horas, resto);
	}
	
	
	/*
	public void inyectarRegistrosJunioAvanzado(Long usuarioId) {
	    Usuario usuario = userRepo.findById(usuarioId).orElseThrow();
	    List<Registro> registros = new ArrayList<>();

	    LocalDate fechaInicio = LocalDate.of(2025, 5, 1);
	    LocalDate fechaFin = LocalDate.of(2025, 5, 30);

	    for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
	        DayOfWeek diaSemana = fecha.getDayOfWeek();

	        if (diaSemana == DayOfWeek.WEDNESDAY) {
	            // Miércoles → no se registran entradas
	            continue;
	        }

	        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
	            // Fin de semana → una sola jornada
	            registros.add(new Registro(
	                usuario,
	                fecha.atTime(7, 30),
	                fecha.atTime(15, 0)
	            ));
	        } else {
	            // Días normales → dos turnos
	            registros.add(new Registro(
	                usuario,
	                fecha.atTime(7, 30),
	                fecha.atTime(12, 30)
	            ));
	            registros.add(new Registro(
	                usuario,
	                fecha.atTime(17, 0),
	                fecha.atTime(21, 30)
	            ));
	        }
	    }

	    regRepo.saveAll(registros);
	    System.out.println("✅ Registros avanzados de junio 2025 insertados: " + registros.size());
	}
*/
}
