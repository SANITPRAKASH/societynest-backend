package com.societynest.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        System.out.println("🔍 [JWT Filter] Request URI: " + request.getRequestURI());
        System.out.println("🔍 [JWT Filter] Authorization Header: " + authorizationHeader);

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            System.out.println("🔍 [JWT Filter] Extracted JWT: " + jwt.substring(0, 20) + "...");
            
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("✅ [JWT Filter] Extracted username: " + username);
            } catch (Exception e) {
                System.out.println("❌ [JWT Filter] JWT extraction failed: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("⚠️ [JWT Filter] No Bearer token found in header");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("✅ [JWT Filter] User details loaded: " + userDetails.getUsername());

                if (jwtUtil.validateToken(jwt, userDetails)) {
                    System.out.println("✅ [JWT Filter] Token is VALID for user: " + username);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("✅ [JWT Filter] Authentication set in SecurityContext");
                } else {
                    System.out.println("❌ [JWT Filter] Token validation FAILED for user: " + username);
                }
            } catch (Exception e) {
                System.out.println("❌ [JWT Filter] Error during authentication: " + e.getMessage());
                e.printStackTrace();
            }
        }

        chain.doFilter(request, response);
    }
}