package com.example.authenticationserver.command.api.controller.auth;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import com.example.authenticationserver.query.api.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.events.user.RefreshAccessTokenForUserProfileCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginRestController {
  private final CommandGateway commandGateway;
  private final UserProfileCommandService userProfileCommandService;
  @Autowired
  public LoginRestController(CommandGateway commandGateway, UserProfileCommandService userProfileCommandService) {
    this.commandGateway = commandGateway;
    this.userProfileCommandService = userProfileCommandService;
  }
  @PostMapping("/login")
  public TokenSummary login(@RequestParam String username,
                            @RequestParam String password){
    var userDetails = userProfileCommandService.authenticationUser(new UsernamePasswordAuthenticationToken(username,password));

    var command = GenerateRefreshTokenForUserProfileCommand.builder()
        .userId(userDetails.getUserProfileEntity().getUserId())
        .build();

    return commandGateway.sendAndWait(command);
  }
  @PostMapping("/refresh")
  public TokenAuthorizationCodeDTO refresh(@RequestParam String refreshToken){
    String userId = JwtTokenUtils.extractClaims(refreshToken).getSubject();
    var command = RefreshAccessTokenForUserProfileCommand.builder()
        .userId(userId)
        .refreshToken(refreshToken)
        .build();

    return commandGateway.sendAndWait(command);
  }
}
