package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.service.UserProfileService;
import com.example.authenticationserver.dto.TokenDTO;
import tokenlib.util.tokenenum.TokenFields;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.commands.user.RefreshAccessTokenForUserProfileCommand;
import com.project.core.events.user.JwkTokenInfoEvent;
import com.project.core.events.user.JwtTokenInfoEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Aggregate
public class UserProfileAggregate {
  @AggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private String role;
  private String tokenId;
  private Date createdAt;
  private Date lastUpdatedAt;
  private Date deleteAt;
  public UserProfileAggregate() {
  }

  @CommandHandler
  public UserProfileAggregate(CreateUserProfileCommand command) {
    var event = UserProfileCreatedEvent.builder()
      .userId(command.getUserId())
      .userName(command.getUserName())
      .passwordHash(command.getPasswordHash())
      .userStatus("CREATED")
      .role("ROLE_USER")
      .createdAt(new Date())
      .build();
    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(UserProfileCreatedEvent event) {
    this.userId = event.getUserId();
    this.userName = event.getUserName();
    this.passwordHash = event.getPasswordHash();
    this.userStatus = event.getUserStatus();
    this.role = event.getRole();
    this.createdAt = event.getCreatedAt();
  }

  @CommandHandler
  public TokenDTO handle(GenerateRefreshTokenForUserProfileCommand command, @NonNull UserProfileService service) {
    UUID tokenId = UUID.randomUUID();

    var map = service.generateJwtTokensMap(
      command.getUserId(),
      tokenId.toString());
    if(service.getEventClass().equals(JwkTokenInfoEvent.class)){
      var event = JwkTokenInfoEvent.builder()
        .userId(command.getUserId())
        .publicKey(map.get(TokenFields.PUBLIC_KEY.getValue()))
        .kid(tokenId.toString())
        .build();
      AggregateLifecycle.apply(event);
    } else {
      var event = JwtTokenInfoEvent.builder()
        .userId(command.getUserId())
        .tokenId(tokenId.toString())
        .build();
      AggregateLifecycle.apply(event);
    }
    return service.makeTokenDTO(map, tokenId.toString());
  }

  @EventSourcingHandler
  public void on(JwtTokenInfoEvent event) {
    this.tokenId = event.getTokenId();
  }
  @CommandHandler
  public TokenDTO handle(RefreshAccessTokenForUserProfileCommand command, @NonNull UserProfileService service) {
    UUID tokenId = UUID.randomUUID();
    var map = service.refreshToken(
      command.getRefreshToken(),
      tokenId.toString());
    if(service.getEventClass().equals(JwkTokenInfoEvent.class)){
      var event = JwkTokenInfoEvent.builder()
        .userId(command.getUserId())
        .publicKey(map.get(TokenFields.PUBLIC_KEY.getValue()))
        .kid(tokenId.toString())
        .lastTokenId(map.get("last_token_id"))
        .build();
      AggregateLifecycle.apply(event);
    } else {
      var event = JwtTokenInfoEvent.builder()
        .userId(command.getUserId())
        .tokenId(tokenId.toString())
        .build();
      AggregateLifecycle.apply(event);
    }

    return service.makeTokenDTO(map, tokenId.toString());
  }
}
