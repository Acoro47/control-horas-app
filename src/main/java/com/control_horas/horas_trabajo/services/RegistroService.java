package com.control_horas.horas_trabajo.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.control_horas.horas_trabajo.dtos.web.RegistroDTO;
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
	
	
	public List<RegistroDTO> mapearRegistros(Long id, LocalDate desde, LocalDate hasta){
		List<Registro> registros = regRepo.findByUsuarioId(id).stream()
				.filter(r -> r.getHoraEntrada() != null)
				.filter(r -> {
					LocalDate fecha = r.getHoraEntrada().toLocalDate();
					return !fecha.isBefore(desde) && !fecha.isAfter(hasta);
				})
				.sorted(Comparator.comparingInt(r ->
				r.getHoraEntrada().getDayOfMonth()
				))
				.toList();
		return registros.stream()
				.map(r -> {
					LocalDateTime entrada = r.getHoraEntrada();
					LocalDateTime salida = r.getHoraSalida();
					
					String duracionBase = "";
					String duracionExtra = "";
					String duracionTotal = "";
					
					if (entrada != null && salida != null) {
						long min = Duration.between(entrada, salida).toMinutes();
						long base = Math.min(min, 240);
						long extra = Math.max(0, min - 240);
						
						duracionBase = (base / 60) + "h " + (base % 60) + "m";
						duracionExtra = (extra / 60) + "h " + (extra % 60) + "m";
						duracionTotal = (min / 60) + "h " + (min % 60) + "m";
					}
					return new RegistroDTO(entrada, salida, duracionBase, duracionExtra, duracionTotal);
				})
				.toList();
	}
}
