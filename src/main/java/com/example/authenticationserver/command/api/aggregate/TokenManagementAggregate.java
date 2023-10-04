package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.token.GenerateTokenCommand;
import com.project.core.events.token.TokenGeneratedEvent;
import com.project.core.queries.FetchResourceServersQuery;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.HashMap;
import java.util.List;

@Aggregate
public class TokenManagementAggregate {
  @AggregateIdentifier
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  private List<String> accessToken;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;

  public TokenManagementAggregate() {
  }
  @CommandHandler
  public TokenManagementAggregate(GenerateTokenCommand command){

    var claims = new HashMap<String,Object>();
    claims.put("userId",command.getUserId());
    claims.put("scope",command.getScope());

    List<String> accessTokens = JwtTokenUtils.generateTokenForResourceServices(command.getResourceServerDTOList());

    var event = TokenGeneratedEvent.builder()
        .tokenId(command.getTokenId())
        .userId(command.getUserId())
        .clientId(command.getClientId())
        .scope(command.getScope())
        .tokenType("bearer")
        .accessToken(accessTokens)
        .expires_in(60000)
        .refreshToken(JwtTokenUtils.generateToken("AuthServer",60000,claims))
        .refresh_expires_in(600000)
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
