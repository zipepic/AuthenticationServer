package com.example.authenticationserver.security.filter.token;

import com.example.authenticationserver.security.UserProfileDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenGenerationFilter extends OncePerRequestFilter {
  private final CommandGateway commandGateway;
  private final ObjectMapper objectMapper;

  public TokenGenerationFilter(CommandGateway commandGateway, ObjectMapper objectMapper) {
    this.commandGateway = commandGateway;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      var userDetails = (UserProfileDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//      var command = RefreshAccessTokenForUserProfileCommand.builder()
//        .userId(userDetails.getUserProfileEntity().getUserId())
//        .refreshToken(request.getAttribute("token").toString())
//        .build();
//      var tokenDTO = commandGateway.sendAndWait(command);
      if(true)
          throw new RuntimeException("Unsupoorted method");

      response.setStatus(HttpStatus.OK.value());
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.getWriter().write(objectMapper.writeValueAsString(null));
      response.getWriter().flush();
  }
}
