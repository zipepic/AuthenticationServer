package com.example.authenticationserver.config;

import com.example.authenticationserver.service.UserProfileDetailsService;
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
  private final UserProfileDetailsService userProfileDetailsService;

  public LoadUserFromDatabaseFilterByJwt(UserProfileDetailsService userProfileDetailsService) {
    this.userProfileDetailsService = userProfileDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      var claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getCredentials();

      String userId = claims.getSubject();
      UserDetails userDetails = userProfileDetailsService.loadUserByUserId(userId);
      UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userDetails, claims, userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }catch (Exception e){
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    }

    filterChain.doFilter(request, response);
  }
}
