package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateOneTimeCodeUserProfileCommand;
import com.project.core.commands.user.UseOneTimeCodeCommand;
import com.project.core.events.user.OneTimeCodeUserProfileGeneratedEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.UUID;
@Slf4j
@Aggregate
public class UserProfileAggregate {
  @AggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String code;
  private String userStatus;
  private Date createdAt;
  private Date lastUpdatedAt;
  private Date deleteAt;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserProfileAggregate() {
  }
  @CommandHandler
  public UserProfileAggregate(CreateUserProfileCommand command){
    UserProfileCreatedEvent event =
      UserProfileCreatedEvent.builder()
        .userId(command.getUserId())
        .userName(command.getUserName())
        .passwordHash(command.getPasswordHash())
        .userStatus(command.getUserStatus())
        .createdAt(command.getCreatedAt())
        .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(UserProfileCreatedEvent event){
    this.userId = event.getUserId();
    this.userName = event.getUserName();
    this.passwordHash = event.getPasswordHash();
    this.userStatus = event.getUserStatus();
    this.createdAt = event.getCreatedAt();
  }
  @CommandHandler
  public String handle(GenerateOneTimeCodeUserProfileCommand command){
    if(!passwordEncoder.matches(command.getPasswordHash(), this.passwordHash)){
      throw new IllegalArgumentException("Password is not correct");
    }
    // TODO implement code generation with JWT
    String code = UUID.randomUUID().toString();

    log.info("Code -> {}", code);

    OneTimeCodeUserProfileGeneratedEvent event = OneTimeCodeUserProfileGeneratedEvent.builder()
      .userId(command.getUserId())
      .code(code)
      .build();
    AggregateLifecycle.apply(event);
    return code;
  }
  @EventSourcingHandler
  public void on(OneTimeCodeUserProfileGeneratedEvent event){
    this.code = event.getCode();
  }

  @CommandHandler
  public TokenSummary handle(UseOneTimeCodeCommand command){
    OneTimeCodeUserProfileGeneratedEvent event = OneTimeCodeUserProfileGeneratedEvent.builder()
      .userId(command.getUserId())
      .code(null)
      .build();
    AggregateLifecycle.apply(event);
    // TODO implement token generation
    return TokenSummary.builder()
      .access_token("access_token")
      .expires_in(60000)
      .refresh_expires_in(60000*7)
      .refresh_token("refresh_token")
      .token_type("bearer")
      .id_token("id_token")
      .not_before_policy("not_before_policy")
      .session_state("session_state")
      .scope("scope")
      .build();
  }
}
