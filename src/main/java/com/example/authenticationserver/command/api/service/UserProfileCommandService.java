package com.example.authenticationserver.command.api.service;

import com.project.core.commands.user.CreateUserProfileCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserProfileCommandService {
  private final CommandGateway commandGateway;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  public UserProfileCommandService(CommandGateway commandGateway, PasswordEncoder passwordEncoder) {
    this.commandGateway = commandGateway;
    this.passwordEncoder = passwordEncoder;
  }
  public CompletableFuture<Object> register(Authentication authentication){
    UUID uuid = UUID.randomUUID();
    //TODO validate username
    CreateUserProfileCommand command =
      CreateUserProfileCommand.builder()
        .userId(uuid.toString())
        .userName(authentication.getName())
        .passwordHash(passwordEncoder.encode(authentication.getCredentials().toString()))
        .build();

    return commandGateway.send(command);
  }
  }
