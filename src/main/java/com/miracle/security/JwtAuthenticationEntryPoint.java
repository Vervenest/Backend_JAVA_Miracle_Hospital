package com.miracle.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {

        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

        String acceptHeader = request.getHeader("Accept");
        String requestURI   = request.getRequestURI();

        // ✅ FIX: Browser/admin requests → redirect to login page
        // API requests (Accept: application/json) → return JSON 401
        boolean isApiRequest = (acceptHeader != null && acceptHeader.contains("application/json"))
                || requestURI.startsWith("/api/");

        if (isApiRequest) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            final Map<String, Object> body = new HashMap<>();
            body.put("status",  HttpServletResponse.SC_UNAUTHORIZED);
            body.put("error",   "Unauthorized");
            body.put("message", e.getMessage());
            body.put("path",    request.getServletPath());
            new ObjectMapper().writeValue(response.getOutputStream(), body);
        } else {
            // Browser request → redirect to admin login
            response.sendRedirect("/admin/login");
        }
    }
}