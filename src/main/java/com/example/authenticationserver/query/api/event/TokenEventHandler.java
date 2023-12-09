package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.token.TokenEntity;
import com.example.authenticationserver.query.api.data.token.TokenRepository;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenEventHandler {
    private final TokenRepository tokenRepository;
    @Autowired
    public TokenEventHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    @EventHandler
    public void on(TokenCreatedEvent event) {
        var tokenEntity = new TokenEntity();
        tokenEntity.setUserId(event.getUserId());
        tokenEntity.setUserRole(event.getRole());
        tokenEntity.setStatus(event.getStatus());
        if(event.getAuthProvider().equals("GITHUB"))
            tokenEntity.setGithubId(event.getProviderId());
        else if(event.getAuthProvider().equals("GOOGLE"))
            tokenEntity.setGoogleId(event.getProviderId());

        tokenRepository.save(tokenEntity);
    }
    @EventHandler
    public void on(TokenCanceledEvent event) {
        tokenRepository.deleteById(event.getUserId());
    }
}
