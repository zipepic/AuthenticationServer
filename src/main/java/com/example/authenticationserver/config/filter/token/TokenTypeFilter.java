package com.example.authenticationserver.config.filter.token;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenTypeFilter extends OncePerRequestFilter {
  private final JwtRefreshFilter jwtRefreshFilter;

  public TokenTypeFilter(JwtRefreshFilter jwtRefreshFilter) {
    this.jwtRefreshFilter = jwtRefreshFilter;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      var claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();
      if(claims.get("token_type").equals("refresh_token")){
        jwtRefreshFilter.doFilterInternal(request,response,filterChain);
      }else {
        filterChain.doFilter(request, response);
      }
  }
}
