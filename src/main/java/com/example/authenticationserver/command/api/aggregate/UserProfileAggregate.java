package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.example.authenticationserver.query.api.dto.TokenDTO;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.events.user.RefreshAccessTokenForUserProfileCommand;
import com.project.core.events.user.RefreshTokenForUserProfileGeneratedEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aggregate
public class UserProfileAggregate {
  private final static Integer ACCESS_EXPIRATION_TIME = 60_000;
  private final static Integer REFRESH_EXPIRATION_TIME = 6_000_000;
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
  public TokenDTO handle(GenerateRefreshTokenForUserProfileCommand command) {
    UUID tokenId = UUID.randomUUID();
    var refreshToken = Jwts.builder()
      .setSubject(command.getUserId())
      .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
      .addClaims(Map.of("token_type","refresh_token"))
      .setId(tokenId.toString());
    String signRefreshToken = JwtTokenUtils.signAndCompactWithDefaults(refreshToken);

    var accessToken = Jwts.builder()
      .setSubject(command.getUserId())
      .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
      .addClaims(Map.of("token_type","access_token"));

    var event = RefreshTokenForUserProfileGeneratedEvent.builder()
      .userId(command.getUserId())
      .refreshToken(signRefreshToken)
      .tokenId(tokenId.toString())
      .build();

    AggregateLifecycle.apply(event);

    return TokenSummary.builder()
      .accessToken(JwtTokenUtils.signAndCompactWithDefaults(accessToken))
      .expiresIn(ACCESS_EXPIRATION_TIME)
      .refreshExpiresIn(REFRESH_EXPIRATION_TIME)
      .refreshToken(signRefreshToken)
      .tokenType("Bearer")
      .tokenId(tokenId.toString())
      .build();
  }

  @EventSourcingHandler
  public void on(RefreshTokenForUserProfileGeneratedEvent event) {
    this.tokenId = event.getTokenId();
  }
  @CommandHandler
  public TokenDTO handle(RefreshAccessTokenForUserProfileCommand command){
    TokenDTO tokenDTO = JwtTokenUtils.refresh(command.getClaims());

    var event = RefreshTokenForUserProfileGeneratedEvent.builder()
      .userId(command.getUserId())
      .refreshToken(tokenDTO.getRefreshToken())
      .tokenId(tokenDTO.getTokenId())
      .build();

    AggregateLifecycle.apply(event);

    return tokenDTO;
  }

}
