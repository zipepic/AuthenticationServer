package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.token.AccessToken;
import com.example.authenticationserver.query.api.data.token.TokenEntity;
import com.example.authenticationserver.query.api.data.token.TokenRepository;
import com.project.core.events.token.TokenGeneratedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
    var list = new ArrayList<AccessToken>();
    for (String accessToken : event.getAccessToken()) {
      AccessToken accessToken1 = new AccessToken();
      accessToken1.setAccessToken(accessToken);
      accessToken1.setTokenEntity(tokenEntity);
      list.add(accessToken1);
    }
    tokenEntity.setAccessTokens(list);
    tokenRepository.save(tokenEntity);
  }
}
