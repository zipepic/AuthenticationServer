package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.dto.TokenSummary;
import com.example.authenticationserver.dto.TokenDTO;
import com.example.authenticationserver.test.JwkManager;
import com.example.authenticationserver.util.AppConstants;
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
import java.util.HashMap;
import java.util.Map;
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
  public TokenDTO handle(GenerateRefreshTokenForUserProfileCommand command) {
    UUID tokenId = UUID.randomUUID();

    Map<String,String> tokenMap;
    if(command.getTokenType().equals("JWT")){
      tokenMap = generateTokens(tokenId.toString());
    }else {
      try {
        tokenMap = JwkManager.generateJwtTokens(command.getUserId(), tokenId.toString());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }


    var event = RefreshTokenForUserProfileGeneratedEvent.builder()
      .userId(command.getUserId())
      .tokenId(tokenId.toString())
      .build();

    AggregateLifecycle.apply(event);

    return TokenSummary.builder()
      .accessToken(tokenMap.get("access"))
      .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
      .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
      .refreshToken(tokenMap.get("refresh"))
      .tokenType("Bearer")
      .tokenId(tokenId.toString())
      .build();
  }
private Map<String, String> generateTokens(String kid){
  var refreshToken = Jwts.builder()
    .setSubject(userId)
    .setExpiration(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal()))
    .addClaims(Map.of("token_type","refresh_token"))
    .setId(kid);
  String signRefreshToken = JwtTokenUtils.signAndCompactWithDefaults(refreshToken);

  var accessToken = Jwts.builder()
    .setSubject(userId)
    .setExpiration(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal()))
    .addClaims(Map.of("token_type","access_token"));

  String signAccessToken = JwtTokenUtils.signAndCompactWithDefaults(accessToken);

  Map<String, String> tokenMap = new HashMap<>();
  tokenMap.put("refresh", signRefreshToken);
  tokenMap.put("access", signAccessToken);
  return tokenMap;
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
      .tokenId(tokenDTO.getTokenId())
      .build();

    AggregateLifecycle.apply(event);

    return tokenDTO;
  }

}
