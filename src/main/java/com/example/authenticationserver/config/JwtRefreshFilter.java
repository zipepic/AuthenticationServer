package com.example.authenticationserver.config;

import com.example.authenticationserver.security.UserProfileDetails;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.queries.user.ValidateRefreshTokenForUserProfileQuery;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshFilter extends OncePerRequestFilter {
  private final QueryGateway queryGateway;

  public JwtRefreshFilter(QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    var userProfileDetails = (UserProfileDetails) authentication.getPrincipal();

    var claims = (Claims) authentication.getCredentials();

    try {
      String tokenType = claims.get("token_type", String.class);
      if(tokenType.equals("refresh_token")){

        var query = ValidateRefreshTokenForUserProfileQuery.builder()
          .userId(userProfileDetails.getUserProfileEntity().getUserId())
          .tokenId(claims.getId())
          .build();

        if(!queryGateway.query(query,Boolean.class).join())
          throw new RuntimeException("Invalid refresh token");
      }
    }catch (Exception e){
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
    }
    filterChain.doFilter(request, response);
  }
}
