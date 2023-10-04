package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.token.TokenEntity;
import com.example.authenticationserver.query.api.data.token.TokenRepository;
import com.project.core.events.token.TokenGeneratedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class TokenManagementEventHandler {
  private final TokenRepository tokenRepository;

  public TokenManagementEventHandler(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @EventHandler
  public void handle(TokenGeneratedEvent event){
    TokenEntity tokenEntity = new TokenEntity();
    BeanUtils.copyProperties(event,tokenEntity);
    tokenRepository.save(tokenEntity);
  }
}
