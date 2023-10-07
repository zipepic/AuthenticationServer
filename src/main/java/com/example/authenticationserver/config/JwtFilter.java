package com.example.authenticationserver.config;

import com.example.authenticationserver.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class JwtFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authToken = request.getHeader("Authorization");
      if (authToken != null && !authToken.isBlank() && authToken.startsWith("Bearer ")) {
        authToken = authToken.replace("Bearer ", "");
        String userId = JwtTokenUtils.getUserName(authToken);
        //TODO implements user details service
        UsernamePasswordAuthenticationToken authenticationToken
          = new UsernamePasswordAuthenticationToken("user", null, null);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    }

    filterChain.doFilter(request, response);
  }
}
