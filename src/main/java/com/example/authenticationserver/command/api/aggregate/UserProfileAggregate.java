package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.user.CreateUserProfileCommand;
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

@Slf4j
@Aggregate
public class UserProfileAggregate {
  @AggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private Date createdAt;
  private Date lastUpdatedAt;
  private Date deleteAt;
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
}
