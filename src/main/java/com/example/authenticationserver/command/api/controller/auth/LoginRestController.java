package com.example.authenticationserver.command.api.controller.auth;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
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
}
