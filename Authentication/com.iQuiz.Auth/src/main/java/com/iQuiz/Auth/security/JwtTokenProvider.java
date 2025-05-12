package com.iQuiz.Auth.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import com.iQuiz.Auth.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
	
	private final JwtKeyStore jwtKeyStore;
	private final long jwtExpirationInMs;
	
	public JwtTokenProvider(JwtKeyStore jwtKeyStore, @Value("${app.jwt.expiration}") long jwtExpirationInMs) {
		this.jwtKeyStore = jwtKeyStore;
		this.jwtExpirationInMs = jwtExpirationInMs;
		logger.info("Jwt Token Provider is initialized with expiration {} ms", jwtExpirationInMs);
	}
	
	public String generateToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
		String tokenId = UUID.randomUUID().toString();
		logger.info("Generating token for email: " + user.getEmail());
		
		Map<String, Object> claims = new HashMap<String, Object>();
		claims.put("role", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()));
		claims.put("userId", user.getId());
		
		return Jwts.builder()
				.setSubject(user.getEmail())
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.setId(tokenId)
				.setHeaderParam("kid", jwtKeyStore.getCurrentKeyId())
				.signWith(jwtKeyStore.getCurrentKey(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	public String extractTokenId(String token) {
		Claims claim = Jwts.parserBuilder()
				.setSigningKey(jwtKeyStore.getCurrentKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claim.getId();
	}
	
	public String getEmailFromToken(String token) {
		logger.debug("Extracting email from token");
		Claims claims = parseToken(token).getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.warn("Token expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT format: {}", ex.getMessage());
        } catch (SignatureException ex) {
            logger.error("JWT signature validation failed: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
	
	private Jws<Claims> parseToken(String token){
		Jwt<?, ?> header = Jwts.parserBuilder().build().parse(token);
		String keyId = ((JwsHeader<?>) header.getHeader()).getKeyId();
		logger.debug("Parsing token using Key Id {}", keyId);

        Key signingKey = jwtKeyStore.getKeyForToken(keyId);

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
	}
}
