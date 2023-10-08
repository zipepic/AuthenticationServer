package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.query.api.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.events.code.AuthorizationCodeGeneratedEvent;
import com.project.core.events.code.AuthorizationCodeUsedEvent;
import io.jsonwebtoken.Jwts;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Aggregate
public class AuthorizationCodeFlowAggregate {
  private final static Integer ACCESS_EXPIRATION_TIME = 60_000;
  private final static Integer REFRESH_EXPIRATION_TIME = 6_000_000;

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
  public TokenAuthorizationCodeDTO handle(UseAuthorizationCodeCommand command){
    if(this.status.equals("USED")){
      throw new IllegalStateException("Authorization code already used");
    }
    var event = AuthorizationCodeUsedEvent.builder()
      .code(command.getCode())
      .status("USED")
      .build();

    AggregateLifecycle.apply(event);



    return TokenAuthorizationCodeDTO.builder()
      .accessToken(generateToken(ACCESS_EXPIRATION_TIME))
      .expiresIn(ACCESS_EXPIRATION_TIME)
      .refreshToken(generateToken(REFRESH_EXPIRATION_TIME))
      .refreshExpiresIn(REFRESH_EXPIRATION_TIME)
      .tokenType("Bearer")
      .build();
  }
  @EventSourcingHandler
  public void on(AuthorizationCodeUsedEvent event){
    this.status = event.getStatus();
  }

  private String generateToken(long expiration) {
    var claims = new HashMap<String,Object>();
    claims.put("scope",this.scope);
    claims.put("type", "Bearer");
    var jwt = Jwts.builder()
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .setSubject(this.userId)
      .addClaims(claims);

    return JwtTokenUtils.signAndCompactWithDefaults(jwt);
  }
}
