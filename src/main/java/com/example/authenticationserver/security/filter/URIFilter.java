package com.example.authenticationserver.security.filter;

import com.example.authenticationserver.security.filter.token.JwtRefreshFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
public class URIFilter extends OncePerRequestFilter {
  private final JwtRefreshFilter jwtRefreshFilter;

  public URIFilter(JwtRefreshFilter jwtRefreshFilter) {
    this.jwtRefreshFilter = jwtRefreshFilter;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      log.info("request url-> {}", request.getRequestURI());
      if(request.getRequestURI().equals("/auth/refresh")){
        jwtRefreshFilter.doFilterInternal(request,response,filterChain);
      }else {
        filterChain.doFilter(request, response);
      }
  }
}
