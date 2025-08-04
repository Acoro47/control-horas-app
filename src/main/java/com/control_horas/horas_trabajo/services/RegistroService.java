package com.control_horas.horas_trabajo.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.control_horas.horas_trabajo.dtos.web.RegistroDTO;
import com.control_horas.horas_trabajo.dtos.web.ResumenDiaDTO;
import com.control_horas.horas_trabajo.entities.Registro;
import com.control_horas.horas_trabajo.repositories.RegistroRepository;



@Service
@Transactional(readOnly = true)
public class RegistroService {
	
	private final Logger logger = LoggerFactory.getLogger(RegistroService.class);
	
	private final RegistroRepository regRepo;
	
	public RegistroService(RegistroRepository repo) {
		this.regRepo = repo;
	}
	
	public Map<LocalDate, Long> calcularTiempoDia(Long usuarioId, LocalDate desde, LocalDate hasta){
		List<Registro> registros = registrosFiltrados(usuarioId, desde, hasta);
		
		Map<LocalDate, Long> minutosDia =  registros.stream()
				.collect(Collectors.groupingBy(
						r -> r.getHoraEntrada().toLocalDate(),
						Collectors.summingLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
						));
		
		logger.info("Minutos dia: {}", minutosDia);
		
		return minutosDia;
	}
	
	public Map<YearMonth, Long> calcularTiempoMes(Long usuarioId, LocalDate desde, LocalDate hasta){
		Map<LocalDate, Long> dias = calcularTiempoDia(usuarioId, desde, hasta);
				
		Map<YearMonth, Long> minutosMes = dias.entrySet().stream()
				.collect(Collectors.groupingBy(
						e -> YearMonth.from(e.getKey()),
						Collectors.summingLong(Map.Entry::getValue)
						));
		
		logger.info("Minutos mes: {}", minutosMes);
		
		return minutosMes;
	}
	
	public Map<Integer, Long> calcularTiempoAnio(Long usuarioId, LocalDate desde, LocalDate hasta){
		Map<LocalDate, Long> dias = calcularTiempoDia(usuarioId, desde, hasta);
				
		Map<Integer, Long> minutosAnio = dias.entrySet().stream()
				.collect(Collectors.groupingBy(
						e -> e.getKey().getYear(),
						Collectors.summingLong(Map.Entry::getValue)
						));
		
		logger.info("Minutos Año: {}", minutosAnio);
		
		return minutosAnio;
	}
	
	public String formatoHoras(long minutos) {
		long horas = minutos / 60;
		long resto = minutos % 60;
		
		return String.format("%02d:%02d", horas, resto);
	}
	
	
	public List<RegistroDTO> mapearRegistros(
			Long usuarioId, 
			LocalDate desde, 
			LocalDate hasta
			){
		
		List<Registro> registros = registrosFiltrados(usuarioId, desde, hasta);
				
		
		return registros.stream()
				.map(r -> {
					logger.info("Registros: Entrada: {}, Salida: {}", r.getHoraEntrada(), r.getHoraSalida());
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
	
	public List<ResumenDiaDTO> mapearResumenDiario(
			Long usuarioId,
			LocalDate desde,
			LocalDate hasta
			){
		
		return regRepo.findByUsuarioIdAndHoraEntradaBetweenAndHoraSalidaIsNotNull(
				usuarioId, desde.atStartOfDay(), hasta.atTime(LocalTime.MAX)
				).stream()
				.collect(Collectors.groupingBy(
						r -> r.getHoraEntrada().toLocalDate(),
						TreeMap::new,
						Collectors.toList()
						))
				.entrySet().stream()
				.map(entry -> {
					LocalDate fecha = entry.getKey();
					List<Registro> tramos = entry.getValue();
					tramos.sort(Comparator.comparing(Registro::getHoraEntrada));
					
					LocalTime entrada1 = tramos.size() >= 1 ? tramos.get(0).getHoraEntrada().toLocalTime() : null;
					LocalTime salida1 = tramos.size() >= 1 ? tramos.get(0).getHoraSalida().toLocalTime() : null;
					
					LocalTime entrada2 = tramos.size() >= 2 ? tramos.get(1).getHoraEntrada().toLocalTime() : null;
					LocalTime salida2 = tramos.size() >= 2 ? tramos.get(1).getHoraSalida().toLocalTime() : null;
					
					Long minutosTotales = tramos.stream()
							.mapToLong(r -> Duration.between(r.getHoraEntrada(), r.getHoraSalida()).toMinutes())
							.sum();
					
					return new ResumenDiaDTO(fecha, entrada1, salida1, entrada2, salida2, minutosTotales);
					
				})
				.sorted(Comparator.comparing(ResumenDiaDTO::getFecha))
				.toList();
		
	}
	
	public List<Registro> registrosFiltrados(Long usuarioId, LocalDate desde, LocalDate hasta){
		LocalDateTime inicio = desde.atStartOfDay();
		LocalDateTime fin = hasta.atTime(LocalTime.MAX);
		
		return regRepo
				.findByUsuarioIdAndHoraEntradaBetweenAndHoraSalidaIsNotNull(
						usuarioId, inicio, fin
				).stream()
				.filter(r -> r.getHoraEntrada() != null && r.getHoraSalida() != null)
				.filter(r -> {
					LocalDate f = r.getHoraEntrada().toLocalDate();
							return !f.isBefore(desde) && !f.isAfter(hasta);
				})
				.toList();				
	}
	
	public long extraerMinutosExtraMesLaboral(List<ResumenDiaDTO> resumen, long minutosContrato) {
		return resumen.stream()
				.filter(d -> !isFinDeSemanaOFestivo(d.getFecha()))
				.mapToLong(d -> Math.max(0, d.getMinutosTotales() - minutosContrato))
				.sum();
				
	}
	
	public long extraerMinutosExtraMesFinde(List<ResumenDiaDTO> resumen, long minutosContrato) {
		return resumen.stream()
				.filter(d -> isFinDeSemanaOFestivo(d.getFecha()))
				.mapToLong(d -> Math.max(0, d.getMinutosTotales() - minutosContrato))
				.sum();
				
	}
	
	private boolean isFinDeSemanaOFestivo(LocalDate fecha) {
		var d = fecha.getDayOfWeek();
		return d == java.time.DayOfWeek.SATURDAY || d == java.time.DayOfWeek.SUNDAY;
	}
	
	public String formatearMinutos(long minutos) {
	    long h = minutos / 60;
	    long m = minutos % 60;
	    return String.format("%d:%02d h", h, m);
	  }
	
}
