package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.ApplicationCreateRestModel;
import com.example.authenticationserver.command.api.restmodel.ApplicationLoginRestModel;
import com.example.authenticationserver.command.api.restmodel.ApplicationRegistrationRestModel;
import com.project.core.commands.CreateApplicationCommand;
import com.project.core.commands.LoginApplicationCommand;
import com.project.core.commands.RegisterApplicationCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ApplicationController {
  private final CommandGateway commandGateway;
  @Autowired
  public ApplicationController(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }
  @PostMapping("/register")
  public String register(@RequestBody ApplicationRegistrationRestModel model){
    RegisterApplicationCommand command =
      RegisterApplicationCommand.builder()
        .clientId(model.getClientId())
        .responseType(model.getResponseType())
        .state(model.getState())
        .scope(model.getScope())
        .redirectUrl(model.getRedirectUrl())
        .build();

    return commandGateway.sendAndWait(command);
  }
  @PostMapping("/login")
  public String login(@RequestBody ApplicationLoginRestModel model){
    LoginApplicationCommand command =
      LoginApplicationCommand.builder()
        .clientId(model.getClientId())
        .secret(model.getSecret())
        .grantType(model.getGrantType())
        .code(model.getCode())
        .redirectUrl(model.getRedirectUrl())
        .build();

    return commandGateway.sendAndWait(command);
  }
  @PostMapping("/create")
  public String create(@RequestBody ApplicationCreateRestModel model){
    CreateApplicationCommand command =
      CreateApplicationCommand.builder()
        .clientId(model.getClientId())
        .secret(model.getSecret())
        .build();
    return commandGateway.sendAndWait(command);
  }
}
