package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.token.CancelTokenCreationCommand;
import com.project.core.commands.token.CreateTokenCommand;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import tokenlib.util.jwk.AuthProvider;

@Slf4j
@Aggregate
//TODO rename to TokenManagerAggregate
public class TokenManagerForUserAggregate {
    @AggregateIdentifier
    private String userId;
    private String githubId;
    private String googleId;
    private String userRole;
    private String tokenId;
    private String status;

    public TokenManagerForUserAggregate() {
    }
    @CommandHandler
    public TokenManagerForUserAggregate(CreateTokenCommand command){
        log.info("CreateTokenCommand handled {}", command.getUserId());
        var event = TokenCreatedEvent.builder()
                .userId(command.getUserId())
                .providerId(command.getProviderId())
                .authProvider(command.getAuthProvider())
                .role(command.getRole())
                .status("CREATED")
                .build();
        AggregateLifecycle.apply(event);

    }
    @EventSourcingHandler
    public void on(TokenCreatedEvent event){
        this.userId = event.getUserId();
        this.userRole = event.getRole();
        this.tokenId = event.getUserId();
        if(event.getAuthProvider() == AuthProvider.GITHUB)
            this.githubId = event.getProviderId();
        else if(event.getAuthProvider() == AuthProvider.GOOGLE)
            this.googleId = event.getProviderId();
        this.status = "CREATED";
    }

    @CommandHandler
    public void handle(CancelTokenCreationCommand command){
        log.info("CancelTokenCommand handled {}", command.getUserId());
        var event = TokenCanceledEvent.builder()
                .userId(command.getUserId())
                        .build();
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(TokenCanceledEvent event){
        this.status = "CANCELLED";
    }

}
