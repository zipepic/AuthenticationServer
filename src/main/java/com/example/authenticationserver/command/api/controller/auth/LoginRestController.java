package com.example.authenticationserver.command.api.controller.auth;

import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.dto.TokenDTO;
import com.example.authenticationserver.security.UserProfileDetails;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.events.user.RefreshAccessTokenForUserProfileCommand;
import com.project.core.queries.user.ValidateRefreshTokenForUserProfileQuery;
import io.jsonwebtoken.Claims;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginRestController {
  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;
  private final UserProfileCommandService userProfileCommandService;
  @Autowired
  public LoginRestController(CommandGateway commandGateway, QueryGateway queryGateway, UserProfileCommandService userProfileCommandService) {
    this.commandGateway = commandGateway;
    this.queryGateway = queryGateway;
    this.userProfileCommandService = userProfileCommandService;
  }
  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestParam String username,
                                        @RequestParam String password){
    var userDetails = userProfileCommandService.authenticationUser(new UsernamePasswordAuthenticationToken(username,password));

    var command = GenerateRefreshTokenForUserProfileCommand.builder()
        .userId(userDetails.getUserProfileEntity().getUserId())
        .build();

    return ResponseEntity.ok(commandGateway.sendAndWait(command));
  }
  @GetMapping("/profile")
  public String profile(){
    var userDetails = (UserProfileDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userDetails.getUserProfileEntity().getUserName();
  }
}
