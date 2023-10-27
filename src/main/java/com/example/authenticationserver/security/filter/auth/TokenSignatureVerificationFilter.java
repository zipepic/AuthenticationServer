package com.example.authenticationserver.security.filter.auth;

import com.example.authenticationserver.util.JwkManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenSignatureVerificationFilter extends OncePerRequestFilter {
  private final JwkManager jwkManager;

  public TokenSignatureVerificationFilter(JwkManager jwkManager) {
    this.jwkManager = jwkManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authToken = (String) request.getAttribute("token");
    Claims claims = null;
    try {
      claims = jwkManager.extractClaims(authToken);
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
