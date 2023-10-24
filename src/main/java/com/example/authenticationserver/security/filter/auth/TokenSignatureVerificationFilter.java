package com.example.authenticationserver.security.filter.auth;

import com.example.authenticationserver.util.newutils.JwtManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenSignatureVerificationFilter extends OncePerRequestFilter {
  private final JwtManager jwtManager;

  public TokenSignatureVerificationFilter(JwtManager jwtManager) {
    this.jwtManager = jwtManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      String authToken = request.getHeader("Authorization");

      if (authToken == null || authToken.isBlank() || !authToken.startsWith("Bearer "))
        throw new IllegalArgumentException("Invalid token");

      authToken = authToken.replace("Bearer ", "");
    Claims claims = null;
    try {
      claims = jwtManager.extractClaims(authToken);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    if(!(SecurityContextHolder.getContext().getAuthentication() == null))
        throw new IllegalArgumentException("Security context already contains an authentication object");

      Authentication authentication = new UsernamePasswordAuthenticationToken(null,claims,null);
      SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }
}
