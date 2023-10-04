package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.application.ApplicationCreateRestModel;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.app.CreateApplicationCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/app")
public class ApplicationController {
  private final CommandGateway commandGateway;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public ApplicationController(CommandGateway commandGateway, PasswordEncoder passwordEncoder) {
    this.commandGateway = commandGateway;
    this.passwordEncoder = passwordEncoder;
  }
  @PostMapping("/create")
  public String create(@RequestBody ApplicationCreateRestModel model) {
    CreateApplicationCommand command = buildCreateCommand(model);
    return commandGateway.sendAndWait(command);
  }
  private CreateApplicationCommand buildCreateCommand(ApplicationCreateRestModel model) {
    return CreateApplicationCommand.builder()
      .clientId(model.getClientId())
      .secret(passwordEncoder.encode(model.getSecret()))
      .build();
  }
}
