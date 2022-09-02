package com.github.coding_team_sept.nd_backend.authentication.utils;

import com.github.coding_team_sept.nd_backend.authentication.models.AppUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;


/**
 * JwtUtils contain any methods which deal with JWT token, including:
 * - Token generation
 * - Token validation
 * - Token extraction from claim
 *
 * @author nivratig
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    private final String secretKey = "secret";

    /**
     * Generates a JWT token, containing:
     * - id
     * - username (email)
     * - role
     * - issue date
     * - expiration date
     * - signature
     *
     * @author nivratig
     */
    public String generateToken(AppUserDetails userDetails) {
        final var claims = new HashMap<String, Object>();
        claims.put("id", userDetails.getId());
        claims.put("role", userDetails.getRole().getName().name());
        return Jwts.builder()
                .setClaims(claims) // Should be put first. Otherwise, it will override other claims.
                .setSubject(userDetails.getUsername()) // "Username" is a placeholder of "Email"
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(getExpirationDate())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Date getExpirationDate() {
        int duration = 1000 * 60 * 60 * 24; // 1 day
        return new Date(System.currentTimeMillis() + duration);
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String extractEmailFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractIdFromToken(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public String extractRoleFromToken(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}