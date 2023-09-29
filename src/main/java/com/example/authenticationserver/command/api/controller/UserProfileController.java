package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.user.UserProfileRestModel;
import com.project.core.commands.CreateUserProfileCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserProfileController {
  private final CommandGateway commandGateway;

  @Autowired
  public UserProfileController(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @PostMapping
  public String create(@RequestBody UserProfileRestModel restModel) {
    UUID uuid = UUID.randomUUID();
    CreateUserProfileCommand command = buildCreateUserProfileCommand(restModel, uuid);
    return commandGateway.sendAndWait(command);
  }

  private CreateUserProfileCommand buildCreateUserProfileCommand(UserProfileRestModel restModel, UUID uuid) {
    return CreateUserProfileCommand.builder()
      .userId(uuid.toString())
      .userName(restModel.getUserName())
      .passwordHash(restModel.getPassword())
      .userStatus("CREATE")
      .createdAt(new Date())
      .build();
  }
}
