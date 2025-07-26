package com.control_horas.horas_trabajo.services;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	
	private final Key key;
	
	public JwtService() {
		
		String secret = System.getenv("JWT_SECRET");
		if (secret == null || secret.trim().isEmpty()) {
			logger.debug("JWT_SECRET en entorno " + System.getenv("JWT_SECRET"));
	        throw new IllegalStateException("❌ JWT_SECRET no está definido en el entorno.");
	    }
		
		byte [] keyBytes = Base64.getDecoder().decode(secret);
		logger.debug("Clave decodificada con éxito: Bytes: " + keyBytes.length);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", userDetails.getAuthorities());
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		Date expiration = extractAllClaims(token).getExpiration();
		return expiration.before(new Date());
	}
	
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		}
		catch (ExpiredJwtException e) {
			System.out.println("Token expirado:" + e.getMessage());
			throw e;
		}
		catch (UnsupportedJwtException  e) {
			System.out.println("Token no soportado:" + e.getMessage());
			throw e;
		}
		catch (MalformedJwtException e) {
	        // Token mal formado
	        System.out.println("⚠️ Token mal formado: " + e.getMessage());
	        throw e;
	    } catch (SecurityException e) {
	        // Fallo en la firma
	        System.out.println("⚠️ Firma inválida: " + e.getMessage());
	        throw e;
	    } catch (IllegalArgumentException e) {
	        // Token vacío o null
	        System.out.println("⚠️ Token ilegal: " + e.getMessage());
	        throw e;
	    }
	}

}
