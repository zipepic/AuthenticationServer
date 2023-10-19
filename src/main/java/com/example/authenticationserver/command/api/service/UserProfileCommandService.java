package com.example.authenticationserver.command.api.service;

import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import com.example.authenticationserver.security.UserProfileDetails;
import com.project.core.commands.user.CreateUserProfileCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UserProfileCommandService {
  private final CommandGateway commandGateway;
  private final PasswordEncoder passwordEncoder;
  private final AuthUserProfileProviderImpl authUserProfileProvider;
  @Autowired
  public UserProfileCommandService(CommandGateway commandGateway, PasswordEncoder passwordEncoder, AuthUserProfileProviderImpl authUserProfileProvider) {
    this.commandGateway = commandGateway;
    this.passwordEncoder = passwordEncoder;
    this.authUserProfileProvider = authUserProfileProvider;
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
  public UserProfileDetails authenticationUser(UsernamePasswordAuthenticationToken token){
    var auth = authUserProfileProvider.authenticate(token);
    return (UserProfileDetails) auth.getPrincipal();
  }
  @PreAuthorize("hasRole('ROLE_USER')")
  public void on(){

  }
}
