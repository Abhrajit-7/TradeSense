package com.funWithStocks.FunWithStocks.component;

import com.funWithStocks.FunWithStocks.entity.User;
import com.funWithStocks.FunWithStocks.services.UserServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private JwtTokenUtil jwtHelper;

    @Autowired
    private UserServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {
        // Check if there is already an authenticated user
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            logger.info("Skipping filter for authenticated user");
            filterChain.doFilter(request, response);
            return;
        }
        // Authorization
        String requestHeader = request.getHeader("Authorization");
        if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
            //logger.info("Invalid or missing Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract token
        String token = requestHeader.substring(7); // Extract the token excluding "Bearer "
        String username = null;
        try {
            username = this.jwtHelper.getUsernameFromToken(token);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException e) {
            logger.error("JWT token processing error: {}", e.getMessage());
            // Optionally handle specific exceptions here
            // e.g., return a 401 Unauthorized response
            filterChain.doFilter(request, response);
            return;
        } catch (Exception e) {
            logger.error("Unexpected error while processing JWT token: {}", e.getMessage());
            // Handle other unexpected exceptions
            filterChain.doFilter(request, response);
            return;
        }

        // Validate token and authenticate user
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = userDetailsService.findUserByUsername(username);
            if (userDetails != null && this.jwtHelper.validateToken(token, userDetails.getUsername())) {
                Collection<GrantedAuthority> authorities = userDetailsService.getAuthoritiesForUser(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.info("User details not found or token validation failed");
            }
        }
        filterChain.doFilter(request, response);
    }
}
