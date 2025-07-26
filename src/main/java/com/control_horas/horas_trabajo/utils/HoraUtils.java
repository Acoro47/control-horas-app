package com.control_horas.horas_trabajo.utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class HoraUtils {
	
	public static LocalDateTime redondearMinutos(LocalDateTime original) {
		int minutos = original.getMinute();
		
		List<Integer> permitidos = Arrays.asList(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
		int cercano = permitidos.get(0);
		int diferenciaMinima = Math.abs(minutos - cercano);
		
		for (int valor:permitidos) {
			int diferencia = Math.abs(minutos - valor);
			if (diferencia < diferenciaMinima) {
				cercano = valor;
				diferenciaMinima = diferencia;
			}
		}
		
		return original.withMinute(cercano).withSecond(0).withNano(0);
	}
	

}
