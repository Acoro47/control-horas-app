package com.control_horas.horas_trabajo.controllers.web;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.services.RegistroService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class EstadisticasController {

	private final RegistroService regService;
	private final UsuarioRepository userRepo;

	public EstadisticasController(RegistroService serv, UsuarioRepository repo) {
		this.regService = serv;
		this.userRepo = repo;
	}

	@GetMapping("/estadisticas-horas/{id}")
	public String verEstadisticas(@PathVariable Long id,
									@RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
									@RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
									Model model) {

		Usuario u = userRepo.findById(id).orElseThrow();

		if (desde == null) {
			desde = LocalDate.now();
		}
		if (hasta == null) {
			hasta = LocalDate.now();
		}

		Map<LocalDate, Long> dia = regService.calcularTiempoDia(id, desde, hasta);
		Map<LocalDate, String> horasDia = dia.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> regService.formatoHoras(e.getValue())
						));

		LocalDate inicioAnio = LocalDate.of(LocalDate.now().getYear(), 1, 1);
		LocalDate hoy = LocalDate.now();

		Map<YearMonth, Long> mes = regService.calcularTiempoMes(id, inicioAnio, hoy);
		Map<YearMonth, String> horasMes = mes.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> regService.formatoHoras(e.getValue())
						));

		Map<Integer, Long> year = regService.calcularTiempoAnio(id, inicioAnio, hoy);
		Map<Integer, String> horasYear = year.entrySet().stream()
				.collect(Collectors.toMap(
						Map.Entry::getKey,
						e -> regService.formatoHoras(e.getValue())
						));


		model.addAttribute("usuario", u);
		model.addAttribute("horasDia", horasDia);
		model.addAttribute("horasMes", horasMes);
		model.addAttribute("horasYear", horasYear);
		model.addAttribute("desde", desde);
		model.addAttribute("hasta", hasta);

		return "admin/estadisticas-horas";
	}

}
