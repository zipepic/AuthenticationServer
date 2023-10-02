package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.project.core.commands.GenerateTokenCommand;
import com.project.core.commands.UseAuthorizationCodeCommand;
import com.project.core.queries.app.CheckLoginDataQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserProfileController {
  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserProfileController(CommandGateway commandGateway, QueryGateway queryGateway, PasswordEncoder passwordEncoder) {
    this.commandGateway = commandGateway;
    this.queryGateway = queryGateway;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/token")
  public TokenSummary generateTokens(@RequestParam String grant_type,
                                     @RequestParam String client_id,
                                     @RequestParam String client_secret,
                                     @RequestParam String code,
                                     @RequestParam String redirect_uri){

    CheckLoginDataQuery loginDataQuery =
      CheckLoginDataQuery.builder()
        .clientId(client_id)
        .secret(client_secret)
        .build();
    boolean applicationIsPresent = queryGateway.query(loginDataQuery, Boolean.class).join();
    if(!applicationIsPresent)
      throw new RuntimeException("Application is not present");

    UseAuthorizationCodeCommand command =
      UseAuthorizationCodeCommand.builder()
        .code(code)
        .build();
    String userName = commandGateway.sendAndWait(command);
    String tokenId = UUID.randomUUID().toString();
    GenerateTokenCommand generateTokenCommand =
      GenerateTokenCommand.builder()
        .tokenId(tokenId)
        .userId(userName)
        .clientId(client_id)
        .scope("read")
        .build();
    return commandGateway.sendAndWait(command);
  }
}
