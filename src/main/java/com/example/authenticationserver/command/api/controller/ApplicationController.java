package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.application.ApplicationCreateRestModel;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.app.CreateApplicationCommand;
import com.project.core.queries.ValidateTokenQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
public class ApplicationController {
  private final CommandGateway commandGateway;
  private final PasswordEncoder passwordEncoder;
  private final QueryGateway queryGateway;

  @Autowired
  public ApplicationController(CommandGateway commandGateway, PasswordEncoder passwordEncoder, QueryGateway queryGateway) {
    this.commandGateway = commandGateway;
    this.passwordEncoder = passwordEncoder;
    this.queryGateway = queryGateway;
  }
  @PostMapping("/create")
  public String create(@RequestBody ApplicationCreateRestModel model) {
    CreateApplicationCommand command = CreateApplicationCommand.builder()
      .clientId(model.getClientId())
      .secret(passwordEncoder.encode(model.getSecret()))
      .build();
    return commandGateway.sendAndWait(command);
  }
  @PostMapping("/validate")
  public String validate(@RequestParam("tokenId") String tokenId,
                         @RequestParam("refreshToken") String refreshToken){
    var query = ValidateTokenQuery.builder()
      .tokenId(tokenId)
      .refreshToken(refreshToken)
      .build();
    if(queryGateway.query(query, Boolean.class).join())
      return "OK";
    return "NOT OK";
  }
}
