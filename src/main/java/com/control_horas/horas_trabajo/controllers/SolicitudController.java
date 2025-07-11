package com.control_horas.horas_trabajo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.control_horas.horas_trabajo.entities.EstadoSolicitud;
import com.control_horas.horas_trabajo.entities.SolicitudAcceso;
import com.control_horas.horas_trabajo.repositories.SolicitudAccesoRepository;

@Controller
public class SolicitudController {
	
	@Autowired
	private SolicitudAccesoRepository solRepo;
	
	@GetMapping("/solicitar")
	public String mostrarFormulario(Model model) {
		
		model.addAttribute("solicitud", new SolicitudAcceso());	
		
		return "/solicitudes/formulario";
		
	}
	
	@PostMapping("/solicitar")
	public String procesarSolicitud(@ModelAttribute SolicitudAcceso solicitud, RedirectAttributes redirect) {
		if (solRepo.existsByEmail(solicitud.getEmail())) {
			redirect.addFlashAttribute("error", "Ya existe una solicitud con este correo");
			return "redirect:/login";
		}
		
		solicitud.setEstado(EstadoSolicitud.PENDIENTE);
		solicitud.setFechaSolicitud(LocalDateTime.now());
		solRepo.save(solicitud);
		
		redirect.addFlashAttribute("mensaje", "Tu solicitud ha sido enviada. Recibirás un correo si es aprobada.");
		return "redirect:/login";
	}
	
	@GetMapping("/admin/solicitudes")
	public String verSolicitudesPendientes(Model model) {
		List<SolicitudAcceso> pendientes = solRepo.findByEstado(EstadoSolicitud.PENDIENTE);
		model.addAttribute("pendientes",pendientes);
		return "admin/solicitudes";
	}
	
	@GetMapping("/admin/aprobar/{id}")
	public String aprobar(@PathVariable Long id, RedirectAttributes redirect) {
		SolicitudAcceso solicitud = solRepo.findById(id).orElseThrow();
		solicitud.setEstado(EstadoSolicitud.APROBADA);
		solicitud.setToken(UUID.randomUUID().toString());
		solicitud.setFechaAprobacion(LocalDateTime.now());
		
		solRepo.save(solicitud);
		redirect.addFlashAttribute("mensaje", "Solicitud aprobada. Token generado");
		
		return "redirect:/admin/solicitudes";		
	}
	
	@GetMapping("/admin/rechazar/{id}")
	public String rechazar(@PathVariable Long id, RedirectAttributes redirect) {
		SolicitudAcceso solicitud = solRepo.findById(id).orElseThrow();
		solicitud.setEstado(EstadoSolicitud.RECHAZADA);
		
		solRepo.save(solicitud);
		redirect.addFlashAttribute("mensaje", "Solicitud rechazada");
		
		return "redirect:/admin/solicitudes";
	}
	
	@GetMapping("/activar")
	public String activar(@RequestParam String token, Model model, RedirectAttributes redirect) {
		Optional<SolicitudAcceso> solicitud = solRepo.findByToken(token);
		if(solicitud.isEmpty() || solicitud.get().getEstado() != EstadoSolicitud.APROBADA) {
			redirect.addFlashAttribute("mensaje", "Token inválido o solicitud no aprobada");
			
			return "redirect:/solicitar";
		}
		
		model.addAttribute("usuarioAutorizado", solicitud.get());
		return "solicitud/registro";
	}
	
	
	

}
