package com.example.authenticationserver.config;

import com.example.authenticationserver.service.UserProfileDetailsService;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.queries.user.ValidateRefreshTokenForUserProfileQuery;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Deprecated
public class JwtFilter extends OncePerRequestFilter {

  private final UserProfileDetailsService userProfileDetailsService;
  private final QueryGateway queryGateway;

  public JwtFilter(UserProfileDetailsService userProfileDetailsService, QueryGateway queryGateway) {
    this.userProfileDetailsService = userProfileDetailsService;
    this.queryGateway = queryGateway;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String authToken = request.getHeader("Authorization");

      if (authToken != null && !authToken.isBlank() && authToken.startsWith("Bearer ")) {
        authToken = authToken.replace("Bearer ", "");

        var claims = JwtTokenUtils.extractClaims(authToken);

        String userId = claims.getSubject();
        UserDetails userDetails = userProfileDetailsService.loadUserByUserId(userId);
        UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        if(SecurityContextHolder.getContext().getAuthentication() == null){
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
      }
      request.setAttribute("jwtToken", authToken);
    } catch (Exception e) {

      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
    }

    filterChain.doFilter(request, response);
  }
}
