package com.control_horas.horas_trabajo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController{
	
	@RequestMapping("/error")
	public String manejarError(HttpServletRequest request, Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if (status != null) {
			int codigo = Integer.parseInt(status.toString());
			switch (codigo) {
			case 404:
				model.addAttribute("error", "Página no encontrada (404)");
				break;
			
			case 500:
				model.addAttribute("error", "Error interno del servidor (500)");
				break;
			
			default:
				model.addAttribute("error", "Error inesperado (" + codigo + ")");
				break;
				
			}
		}
		
		return "error";
	}

}
