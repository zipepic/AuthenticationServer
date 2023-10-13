package com.example.authenticationserver.config.filter.token;

import com.example.authenticationserver.security.UserProfileDetails;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshFilter extends OncePerRequestFilter {
  private final TokenGenerationFilter tokenGenerationFilter;

  public JwtRefreshFilter(TokenGenerationFilter tokenGenerationFilter) {
    this.tokenGenerationFilter = tokenGenerationFilter;
  }

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      var userProfileDetails = (UserProfileDetails) authentication.getPrincipal();

      var claims = (Claims) authentication.getCredentials();

      if(!claims.get("token_type").equals("refresh_token"))
        throw new IllegalArgumentException("This is not a refresh token. Token_type -> " + claims.get("token_type"));

      if (!userProfileDetails.getUserProfileEntity().getTokenId().equals(claims.getId()))
        throw new IllegalArgumentException("This token is epsent in the database");

      tokenGenerationFilter.doFilterInternal(request, response,filterChain);
  }
}
