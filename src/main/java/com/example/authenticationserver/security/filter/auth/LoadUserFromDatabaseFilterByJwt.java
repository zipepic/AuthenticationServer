package com.example.authenticationserver.security.filter.auth;

import com.example.authenticationserver.security.service.UserDetailsLoader;
import com.example.authenticationserver.security.service.UserProfileDetailsLoaderService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class LoadUserFromDatabaseFilterByJwt extends OncePerRequestFilter {
  private final UserDetailsLoader userDetailsLoader;

  public LoadUserFromDatabaseFilterByJwt(UserDetailsLoader userDetailsLoader) {
    this.userDetailsLoader = userDetailsLoader;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    var claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();

    String userId = claims.getSubject();

    UserDetails userDetails = userDetailsLoader.loadUserByUserId(userId);

    UsernamePasswordAuthenticationToken authenticationToken =
      new UsernamePasswordAuthenticationToken(userDetails, claims, userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    filterChain.doFilter(request, response);
  }
}
