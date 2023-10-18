package com.example.authenticationserver.security.filter.auth;

import com.example.authenticationserver.test.JwkManager;
import com.nimbusds.jose.jwk.JWKSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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

      if(!(SecurityContextHolder.getContext().getAuthentication() == null))
        throw new IllegalArgumentException("Security context already contains an authentication object");

      Authentication authentication = new UsernamePasswordAuthenticationToken(null,claims,null);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid token");
    }
    filterChain.doFilter(request, response);
  }
}
