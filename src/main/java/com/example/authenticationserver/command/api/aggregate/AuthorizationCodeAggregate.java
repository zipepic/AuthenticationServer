package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.restmodel.TokenInfo;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.events.code.AuthorizationCodeGeneratedEvent;
import com.project.core.events.code.AuthorizationCodeUsedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

@Aggregate
public class AuthorizationCodeAggregate {
  @AggregateIdentifier
  private String code;
  private String userId;
  private String clientId;
  private Date expiresAt;
  private String scope;
  private String status;
  private String sessionId;

  public AuthorizationCodeAggregate() {
  }
  @CommandHandler
  public AuthorizationCodeAggregate(GenerateAuthorizationCodeCommand command){
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
  public TokenInfo handle(UseAuthorizationCodeCommand command){
    if(this.status.equals("USED")){
      throw new IllegalStateException("Authorization code already used");
    }
    var event = AuthorizationCodeUsedEvent.builder()
      .code(command.getCode())
      .status("USED")
      .build();
    AggregateLifecycle.apply(event);

    return TokenInfo.builder()
        .userId(this.userId)
        .clientId(this.clientId)
        .scope(this.scope)
        .build();
  }
  @EventSourcingHandler
  public void on(AuthorizationCodeUsedEvent event){
    this.status = event.getStatus();
  }
}
