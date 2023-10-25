package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.aggregate.service.UserProfileService;
import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.dto.TokenSummary;
import com.example.authenticationserver.dto.TokenDTO;
import com.example.authenticationserver.util.AppConstants;
import com.example.authenticationserver.util.JwkManager;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.events.user.RefreshAccessTokenForUserProfileCommand;
import com.project.core.events.user.RefreshTokenForUserProfileGeneratedEvent;
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
  public TokenDTO handle(GenerateRefreshTokenForUserProfileCommand command,@NonNull UserProfileService userProfileService) {
    UUID tokenId = UUID.randomUUID();

    var event = RefreshTokenForUserProfileGeneratedEvent.builder()
      .userId(command.getUserId())
      .tokenId(tokenId.toString())
      .build();

    AggregateLifecycle.apply(event);
    try {
      var tokenMap = userProfileService.generateJwtTokens(command.getUserId(), tokenId.toString(), command.getTokenType());
      return TokenSummary.builder()
        .accessToken(tokenMap.get("access"))
        .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
        .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
        .refreshToken(tokenMap.get("refresh"))
        .tokenType("Bearer")
        .tokenId(tokenId.toString())
        .build();
    }catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @EventSourcingHandler
  public void on(RefreshTokenForUserProfileGeneratedEvent event) {
    this.tokenId = event.getTokenId();
  }
  @CommandHandler
  public TokenDTO handle(RefreshAccessTokenForUserProfileCommand command, JwkManager jwkManager) {
    try {
      var kid = jwkManager.getJwtHeader(command.getRefreshToken()).getKeyID();
      var tokenMap = jwkManager.refresh(jwkManager.extractClaims(command.getRefreshToken()),kid);

      var event = RefreshTokenForUserProfileGeneratedEvent.builder()
        .userId(command.getUserId())
        .tokenId(kid)
        .build();

      AggregateLifecycle.apply(event);

      return TokenAuthorizationCodeDTO.builder()
        .accessToken(tokenMap.get("access"))
        .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
        .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
        .refreshToken(tokenMap.get("refresh"))
        .tokenType("Barer")
        .tokenId(kid)
        .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
