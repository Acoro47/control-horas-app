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

@Service
public class RegistroService {
	
	private final RegistroRepository regRepo;
	
	public RegistroService(RegistroRepository repo) {
		this.regRepo = repo;
	}
	
	public Map<LocalDate, Long> calcularTiempoDia(Long usuarioId, LocalDate desde, LocalDate hasta){
		List<Registro> registros = regRepo.findById(usuarioId).stream()
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate fecha = r.getHoraEntrada().toLocalDate();
					return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
				})
				.toList();
		
		return registros.stream()
				.collect(Collectors.groupingBy(
						r -> r.getHoraEntrada().toLocalDate(),
						Collectors.summingLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
						));
	}
	
	public Map<YearMonth, Long> calcularTiempoMes(Long usuarioId, LocalDate desde, LocalDate hasta){
		List<Registro> registros = regRepo.findById(usuarioId).stream()
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate fecha = r.getHoraEntrada().toLocalDate();
					return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
				})
				.toList();
		
		return registros.stream()
				.collect(Collectors.groupingBy(
						r -> YearMonth.from(r.getHoraEntrada()),
						Collectors.summingLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
						));
	}
	
	public Map<Integer, Long> calcularTiempoAnio(Long usuarioId, LocalDate desde, LocalDate hasta){
		List<Registro> registros = regRepo.findById(usuarioId).stream()
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate fecha = r.getHoraEntrada().toLocalDate();
					return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
				})
				.toList();
		
		Map<Integer, Long> minutosAnio = registros.stream()
				.collect(Collectors.groupingBy(
						r -> r.getHoraEntrada().getYear(),
						Collectors.summingLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
						));
		
		return minutosAnio;
	}
	
	public String formatoHoras(long minutos) {
		long horas = minutos / 60;
		long resto = minutos % 60;
		
		return String.format("%02d:%02d", horas, resto);
	}

}
