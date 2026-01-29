package com.clouds3.userservice.service.auth;

import com.clouds3.userservice.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${secret-key}")
    private String secret;

    public String createAccessToken(UserEntity user) {

        log.debug("Creating access token for userId={}", user.getId());

        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("role", "ROLE_" + user.getRole().getRoleName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();

        log.debug("Access token successfully created for userId={}", user.getId());

        return token;
    }

    public String extractUserId(String token) {

        log.trace("Extractin user id from JWT");

        return extractAllClaims(token).getSubject();
    }

    public String extractEmail(String token) {

        log.trace("Extracting username from JWT");

        return extractAllClaims(token).get("email", String.class);
    }

    public String extractRole(String token) {

        log.trace("Extracting role from JWT");

        return extractAllClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {

        log.trace("Parsing JWT claims");

        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigninKey() {
        byte[] apiKeySecretBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(apiKeySecretBytes);
    }

    private Boolean isTokenExpired(String token) {

        boolean expired =
                extractExpiration(token).before(new Date());

        if (expired) {
            log.debug("JWT token is expired");
        }

        return expired;
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
}
