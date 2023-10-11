//package com.example.authenticationserver.config.filter.auth;
//
//import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
//import com.example.authenticationserver.security.UserProfileDetails;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Setter;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//@Setter
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//  private AuthUserProfileProviderImpl authUserProfileProvider;
//  private final CommandGateway commandGateway;
//  private final ObjectMapper objectMapper;
//  @Autowired
//  public AuthenticationFilter(AuthUserProfileProviderImpl authUserProfileProvider, CommandGateway commandGateway, ObjectMapper objectMapper) {
//    this.authUserProfileProvider = authUserProfileProvider;
//    this.commandGateway = commandGateway;
//    this.objectMapper = objectMapper;
//    super.setAuthenticationManager((AuthenticationManager) authUserProfileProvider);
//  }
//
//
//  @Override
//  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//    throws AuthenticationException {
//    String username = request.getParameter("username");
//    String password = request.getParameter("password");
//    return authUserProfileProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//  }
//  @Override
//  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//                                          Authentication authResult) throws IOException, ServletException {
//    var userDetails = (UserProfileDetails) authResult.getPrincipal();
//    var command = GenerateRefreshTokenForUserProfileCommand.builder()
//      .userId(userDetails.getUserProfileEntity().getUserId())
//      .build();
//    var tokenDTO = commandGateway.sendAndWait(command);
//
//    response.setStatus(HttpStatus.OK.value());
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.getWriter().write(objectMapper.writeValueAsString(tokenDTO)); // Преобразуйте в JSON и записьте в тело
//    response.getWriter().flush();
//  }
//}
