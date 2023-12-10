package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.jwk.JwkEntity;
import com.example.authenticationserver.query.api.data.jwk.JwkRepository;
import com.example.authenticationserver.query.api.data.token.TokenManagerEntity;
import com.example.authenticationserver.query.api.data.token.TokenRepository;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import com.project.core.events.token.TokenGeneratedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenEventHandler {
    private final TokenRepository tokenRepository;
    private final JwkRepository jwkRepository;
    @Autowired
    public TokenEventHandler(TokenRepository tokenRepository, JwkRepository jwkRepository) {
        this.tokenRepository = tokenRepository;
        this.jwkRepository = jwkRepository;
    }
    @EventHandler
    public void on(TokenCreatedEvent event) {
        var tokenEntity = new TokenManagerEntity();
        tokenEntity.setUserId(event.getTokenFromUserId().getUserId());
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
        tokenRepository.deleteById(event.getTokenFromUserId().getUserId());
    }

    @EventHandler
    public void on(TokenGeneratedEvent event) {
        System.out.println("Public key -> " + event.getJwk());
        var tokenEntity = tokenRepository.findById(event.getTokenId().getUserId()).get();
        tokenEntity.setTokenId(event.getKid());
        var jwk = new JwkEntity();
        jwk.setKid(event.getKid());
        jwk.setPublicKey(event.getJwk());
        jwkRepository.save(jwk);
        tokenRepository.save(tokenEntity);
        if(event.getLastKid() != null)
            jwkRepository.deleteById(event.getLastKid());
    }
}
