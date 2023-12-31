package com.example.authenticationserver.security.filter.auth;

import tokenlib.util.TokenFacade;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SignatureVerificationFilter extends OncePerRequestFilter {
  private final TokenFacade tokenUtils;

  public SignatureVerificationFilter(TokenFacade tokenUtils) {
    this.tokenUtils = tokenUtils;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String authToken = (String) request.getAttribute("token");

    try {
      var claims = tokenUtils.extractClaimsFromToken(authToken);

      if(!(SecurityContextHolder.getContext().getAuthentication() == null))
        throw new IllegalArgumentException("Security context already contains an authentication object");

      Authentication authentication = new UsernamePasswordAuthenticationToken(null,claims,null);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    filterChain.doFilter(request, response);
  }
}
