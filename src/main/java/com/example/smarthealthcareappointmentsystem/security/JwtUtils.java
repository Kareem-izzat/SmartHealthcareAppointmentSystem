package com.example.smarthealthcareappointmentsystem.security;

import com.example.smarthealthcareappointmentsystem.entites.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Component
public class JwtUtils {
    // key and expirtion are in application properties
    // key was randomly generated from an online website
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")   // This matches jwt.expirationMs
    private int jwtExpirationMs;
    public String generateJwtToken(String email, Role role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role",role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public String getEmailFromJwt(String token) {
        return parseClaims(token).getSubject();
    }


    public String getRoleFromJwt(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // if parsing work then token is valid
    public boolean validateJwtToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
