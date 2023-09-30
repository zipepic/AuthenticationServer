package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.project.core.commands.UseOneTimeCodeCommand;
import com.project.core.queries.CheckLoginDataQuery;
import com.project.core.queries.FindUserIdByOneTimeCodeQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    FindUserIdByOneTimeCodeQuery findUserQuery =
      FindUserIdByOneTimeCodeQuery.builder()
        .code(code)
        .build();
    String userId = queryGateway.query(findUserQuery, String.class).join();

    UseOneTimeCodeCommand command =
      UseOneTimeCodeCommand.builder()
        .userId(userId)
        .code(code)
        .build();

    return commandGateway.sendAndWait(command);
  }

  }
