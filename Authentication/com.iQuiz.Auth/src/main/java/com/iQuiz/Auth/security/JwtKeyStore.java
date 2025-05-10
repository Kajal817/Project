package com.iQuiz.Auth.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtKeyStore {
	private static final Logger logger = LoggerFactory.getLogger(JwtKeyStore.class);
	private final Map<String, String> keys = new HashMap<>();
	private final String currentKeyId;
	
	public JwtKeyStore(@Value("${jwt.keys.current}") String currentKeyId, @Value("#{${jwt.keys}}") Map<String, String> keys) {
		this.currentKeyId = currentKeyId;
		this.keys.putAll(keys);
		logger.info("JWT Key Store intialized. Current KeyId: {}", currentKeyId);
	}
	
	
	public Key getKeyById(String keyId) {
		logger.debug("Fetching Key for key id: {}", keyId);
		String secret = keys.get(keyId);
		if(secret == null) {
			logger.error("Key Id {} not found in configuration.", keyId);
			throw new IllegalArgumentException("Invalid Key Id: "+ keyId);
		}
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	public Key getCurrentKey() {
		logger.debug("Fetching current Signing Key (Key Id: {})", currentKeyId);
		return getKeyById(currentKeyId);
	}
	
	public String getCurrentKeyId() {
		return currentKeyId;
	}
}
