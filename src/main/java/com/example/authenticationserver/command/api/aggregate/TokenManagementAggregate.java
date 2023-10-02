package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.GenerateTokenCommand;
import com.project.core.events.TokenGeneratedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class TokenManagementAggregate {
  @AggregateIdentifier
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  private String accessToken;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;

  public TokenManagementAggregate() {
  }
  @CommandHandler
  public TokenManagementAggregate(GenerateTokenCommand command){

    TokenGeneratedEvent event =
      TokenGeneratedEvent.builder()
        .tokenId(command.getTokenId())
        .userId(command.getUserId())
        .clientId(command.getClientId())
        .scope(command.getScope())
        .tokenType("bearer")
        .accessToken("access-token")
        .expires_in(60000)
        .refreshToken("refresh-token")
        .refresh_expires_in(60000)
        .status("CREATED")
        .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(TokenGeneratedEvent event){
    this.tokenId = event.getTokenId();
    this.userId = event.getUserId();
    this.clientId = event.getClientId();
    this.tokenType = event.getTokenType();
    this.accessToken = event.getAccessToken();
    this.expires_in = event.getExpires_in();
    this.refreshToken = event.getRefreshToken();
    this.refresh_expires_in = event.getRefresh_expires_in();
    this.scope = event.getScope();
    this.status = event.getStatus();
  }
}
