package com.control_horas.horas_trabajo.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.control_horas.horas_trabajo.entities.EstadoSolicitud;
import com.control_horas.horas_trabajo.entities.Role;
import com.control_horas.horas_trabajo.entities.SolicitudAcceso;
import com.control_horas.horas_trabajo.entities.Usuario;
import com.control_horas.horas_trabajo.repositories.SolicitudAccesoRepository;
import com.control_horas.horas_trabajo.repositories.UsuarioRepository;
import com.control_horas.horas_trabajo.services.EmailService;

@Controller
public class SolicitudController {
	
	@Autowired
	private SolicitudAccesoRepository solRepo;
	
	@Autowired
	private UsuarioRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private EmailService mailService;
	
	@GetMapping("/solicitar")
	public String mostrarFormulario(Model model) {
		System.out.println("Cargando formulario de solicitud");
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
		
		System.err.println("Enviando mail " + solicitud.toString());
		
		mailService.enviarAprobacion(solicitud);
		redirect.addFlashAttribute("mensaje", "Solicitud aprobada.");
		
		return "redirect:/admin/solicitudes";		
	}
	
	@GetMapping("/admin/rechazar/{id}")
	public String rechazar(@PathVariable Long id, RedirectAttributes redirect) {
		SolicitudAcceso solicitud = solRepo.findById(id).orElseThrow();
		solicitud.setEstado(EstadoSolicitud.RECHAZADA);
		solRepo.save(solicitud);
		
		mailService.enviarRechazo(solicitud);
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
		return "/registro";
	}
	
	
	@PostMapping("/guardarUsuario")
	public String guardarUsuario(@RequestParam String username,
								@RequestParam String email,
								@RequestParam String pass,
								@RequestParam String confirmPass,
								Model model,
								RedirectAttributes redirect) {
		
		Optional<SolicitudAcceso> solicitudOpt = solRepo.findByEmail(email);
		
		if(solicitudOpt.isEmpty() || solicitudOpt.get().getEstado() != EstadoSolicitud.APROBADA) {
			redirect.addFlashAttribute("mensaje", "Solicitud inválida. Vuelve a intentarlo");
			return "redirect:/solicitar";
		}
		
		SolicitudAcceso solicitud = solicitudOpt.get();
		
		if (!pass.equals(confirmPass)) {
			model.addAttribute("usuarioAutorizado", solicitud);
			model.addAttribute("error", "Las contraseñas no coinciden");
			System.out.println("✅ Método guardarUsuario ejecutado con: " + email);
			return "registro";
		}
		
		if (userRepo.existsByMail(email)) {
			model.addAttribute("usuarioAutorizado", solicitud);
			model.addAttribute("error", "Este correo ya está registrado");
			return "redirect:/registro";
		}
		
		Usuario user = new Usuario();
		user.setUsername(username);
		user.setMail(email);
		user.setPassword(encoder.encode(pass));
		user.setRol(Role.USER);
		userRepo.save(user);
		
		
		solicitud.setToken(null);
		solicitud.setEstado(EstadoSolicitud.USADA);
		solRepo.save(solicitud);
		
		redirect.addFlashAttribute("mensaje", "Cuenta creada correctamente.");
		
		return "redirect:/login";	
	}
	
	

}
