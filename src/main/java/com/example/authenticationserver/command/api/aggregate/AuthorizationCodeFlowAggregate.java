package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.util.AppConstants;
import com.example.authenticationserver.util.newutils.JwtManager;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.events.code.AuthorizationCodeGeneratedEvent;
import com.project.core.events.code.AuthorizationCodeUsedEvent;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aggregate
public class AuthorizationCodeFlowAggregate {

  @AggregateIdentifier
  private String code;
  private String userId;
  private String clientId;
  private Date expiresAt;
  private String scope;
  private String status;
  private String sessionId;

  public AuthorizationCodeFlowAggregate() {
  }
  @CommandHandler
  public AuthorizationCodeFlowAggregate(GenerateAuthorizationCodeCommand command){
    String code = UUID.randomUUID().toString();

    var event = AuthorizationCodeGeneratedEvent.builder()
        .code(code)
        .userId(command.getUserId())
        .clientId(command.getClientId())
        .expiresAt(new Date(System.currentTimeMillis() + 60000))
        .scope(command.getScope())
        .status("CREATED")
        .sessionId(command.getSessionId())
        .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(AuthorizationCodeGeneratedEvent event){
    this.code = event.getCode();
    this.userId = event.getUserId();
    this.clientId = event.getClientId();
    this.expiresAt = event.getExpiresAt();
    this.scope = event.getScope();
    this.status = event.getStatus();
    this.sessionId = event.getSessionId();
  }
  @CommandHandler
  public TokenAuthorizationCodeDTO handle(UseAuthorizationCodeCommand command, JwtManager jwtManager){
    String tokeId = UUID.randomUUID().toString();
    if(this.status.equals("USED")){
      throw new IllegalStateException("Authorization code already used");
    }
    var event = AuthorizationCodeUsedEvent.builder()
      .code(command.getCode())
      .status("USED")
      .build();

    AggregateLifecycle.apply(event);

    try {
      var tokenMap = jwtManager.generateJwtTokens(this.userId, tokeId);
      return TokenAuthorizationCodeDTO.builder()
        .accessToken(tokenMap.get("access"))
        .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
        .refreshToken(tokenMap.get("refresh"))
        .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
        .tokenType("Bearer")
        .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


  }
  @EventSourcingHandler
  public void on(AuthorizationCodeUsedEvent event){
    this.status = event.getStatus();
  }
}
