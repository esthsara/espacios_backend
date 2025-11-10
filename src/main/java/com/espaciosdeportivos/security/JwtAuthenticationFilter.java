package com.espaciosdeportivos.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import com.espaciosdeportivos.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import java.io.IOException;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            logger.debug("JWT Filter - Request: {} {}", request.getMethod(), request.getRequestURI());
            logger.debug("JWT Filter - JWT Present: {}", jwt != null);

            if (jwt != null) {
                if (jwtUtils.validateJwtToken(jwt)) {
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    logger.debug("JWT Filter - Valid JWT for user: {}", username);

                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        
                        logger.debug("JWT Filter - User authorities: {}", userDetails.getAuthorities());

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        
                        logger.debug("JWT Filter - Authentication SUCCESS for user: {}", username);
                        
                    } catch (Exception e) {
                        logger.error("JWT Filter - Error loading user details: {}", e.getMessage());
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    logger.warn("JWT Filter - Invalid JWT token");
                    SecurityContextHolder.clearContext();
                }
            } else {
                logger.debug("JWT Filter - No JWT token found");
                SecurityContextHolder.clearContext();
            }
        } catch (Exception e) {
            logger.error("JWT Filter - Cannot set user authentication: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}