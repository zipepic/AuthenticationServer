package com.example.authenticationserver.security.filter.auth;

import com.example.authenticationserver.test.JwkManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWKsSignatureVerificationFilter extends OncePerRequestFilter {
  private final JwkManager jwtTokenGenerator;

  public JWKsSignatureVerificationFilter(JwkManager jwtTokenGenerator) {
    this.jwtTokenGenerator = jwtTokenGenerator;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authToken = request.getHeader("Authorization");

    if (authToken == null || authToken.isBlank() || !authToken.startsWith("Bearer "))
      throw new IllegalArgumentException("Invalid token");

    authToken = authToken.replace("Bearer ", "");
    try {
      var claims = jwtTokenGenerator.verifyAndParseJWT(authToken);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid token");
    }

    if(!(SecurityContextHolder.getContext().getAuthentication() == null))
      throw new IllegalArgumentException("Security context already contains an authentication object");



    filterChain.doFilter(request, response);
  }
}
