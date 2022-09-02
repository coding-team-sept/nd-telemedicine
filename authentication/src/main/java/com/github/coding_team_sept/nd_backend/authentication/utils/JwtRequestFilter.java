package com.github.coding_team_sept.nd_backend.authentication.utils;

import com.github.coding_team_sept.nd_backend.authentication.services.AppUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final var token = parseTokenFromHeader(request);
            if (token != null && jwtUtils.validateToken(token)) {
                final var userDetails = userDetailsService.loadUserByEmail(
                        jwtUtils.extractEmailFromToken(token)
                );
                final var authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        (userDetails == null)
                                ? List.of()
                                : userDetails.getAuthorities()
                );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );
                SecurityContextHolder.
                        getContext().
                        setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}