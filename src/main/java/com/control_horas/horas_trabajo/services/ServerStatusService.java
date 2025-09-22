package com.control_horas.horas_trabajo.services;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ServerStatusService {
	
	private final Logger logger = LoggerFactory.getLogger(ServerStatusService.class);
	
	public boolean checkServerStatus() {
		
		try {
			HttpClient client = HttpClient.newBuilder()
					.connectTimeout(Duration.ofSeconds(5))
					.build();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create("https://control-horas-app-1.onrender.com/api/login"))
					.GET()
					.timeout(Duration.ofSeconds(5))
					.build();
			
			HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
			
			logger.info("Check server status: HTTP " + response.statusCode());
			return response.statusCode() == 200;
			
		} catch (Exception e) {
			logger.error("Error checking server status",e);
			return false;
		}
		
	}
	

}
