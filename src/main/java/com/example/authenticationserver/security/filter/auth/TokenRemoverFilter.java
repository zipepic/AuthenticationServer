package com.example.authenticationserver.security.filter.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenRemoverFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authToken = request.getHeader("Authorization");

      if (authToken == null || authToken.isBlank() || !authToken.startsWith("Bearer "))
        throw new IllegalArgumentException("Invalid token");

      authToken = authToken.replace("Bearer ", "");

      request.setAttribute("token", authToken);
      filterChain.doFilter(request, response);
    }catch (Exception e){
      throw new IllegalArgumentException(e.getMessage());
    }

  }
}
