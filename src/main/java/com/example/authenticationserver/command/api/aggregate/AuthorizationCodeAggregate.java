package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.project.core.commands.GenerateAuthorizationCodeCommand;
import com.project.core.commands.UseAuthorizationCodeCommand;
import com.project.core.events.AuthorizationCodeGeneratedEvent;
import com.project.core.events.AuthorizationCodeUsedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

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
    if(this.status != null && !this.status.equals("USED")){
      throw new IllegalStateException("Authorization code already generated");
    }
    AuthorizationCodeGeneratedEvent event =
      AuthorizationCodeGeneratedEvent.builder()
        .code(command.getCode())
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
  public TokenSummary handle(UseAuthorizationCodeCommand command){
    AggregateLifecycle.apply(AuthorizationCodeUsedEvent.builder()
      .code(command.getCode())
      .status("USED")
      .build());
    return TokenSummary.builder()
      .scope(this.scope)
      .build();
  }
  @EventSourcingHandler
  public void on(AuthorizationCodeUsedEvent event){
    this.status = event.getStatus();
  }
}