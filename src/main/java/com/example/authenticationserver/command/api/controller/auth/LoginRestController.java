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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  @PostMapping("/refresh")
  public TokenDTO refresh(){
    var authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    var claims = (Claims) authentication.getCredentials();
    var userEntity = (UserProfileDetails) authentication.getPrincipal();
    String tokenId = userEntity.getUserProfileEntity().getTokenId();
    if(true)
    if(!tokenId.equals(claims.getId()))
      throw new IllegalArgumentException("Invalid token");
    var command = RefreshAccessTokenForUserProfileCommand.builder()
      .userId(userEntity.getUserProfileEntity().getUserId())
      .claims(claims)
      .build();
    return commandGateway.sendAndWait(command);
  }
}
