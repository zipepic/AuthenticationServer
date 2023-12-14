package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.service.TokenManagerService;
import com.project.core.commands.token.CancelTokenCreationCommand;
import com.project.core.commands.token.CreateTokenCommand;
import com.project.core.commands.token.GenerateJwtTokenCommand;
import com.project.core.commands.token.RefreshJwtTokenCommand;
import com.project.core.dto.TokenDTO;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import com.project.core.events.token.TokenGeneratedEvent;
import lombok.Data;
import lombok.NonNull;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import tokenlib.util.jwk.AuthProvider;
import tokenlib.util.jwk.SimpleJWK;
import tokenlib.util.tokenenum.TokenFields;

import java.util.UUID;

@Aggregate
@Data
//TODO rename to TokenManagerAggregate
public class TokenManagerForUserAggregate {
    @AggregateIdentifier
    private String tokenFromUserId;
    private String githubId;
    private String googleId;
    private String userRole;
    private String tokenId;
    private String status;

    public TokenManagerForUserAggregate() {
    }
    @CommandHandler
    public TokenManagerForUserAggregate(CreateTokenCommand command){
        var event = TokenCreatedEvent.builder()
                .tokenFromUserId(command.getTokenFromUserId())
                .providerId(command.getProviderId())
                .authProvider(command.getAuthProvider())
                .role(command.getRole())
                .status("CREATED")
                .userId(command.getTokenFromUserId().getUserId())
                .build();
        AggregateLifecycle.apply(event);

    }
    @EventSourcingHandler
    public void on(TokenCreatedEvent event){
        this.tokenFromUserId = event.getTokenFromUserId().toString();
        this.userRole = event.getRole();
        if(event.getProviderId() != null&&event.getAuthProvider() == AuthProvider.GITHUB)
            this.githubId = event.getProviderId();
        else if(event.getProviderId() != null&&event.getAuthProvider() == AuthProvider.GOOGLE)
            this.googleId = event.getProviderId();
        this.status = "CREATED";
        System.out.println("Send TokenCreatedEvent");
    }

    @CommandHandler
    public void handle(CancelTokenCreationCommand command){
        var event = TokenCanceledEvent.builder()
                .tokenFromUserId(command.getTokenFromUserId())
                .userId(command.getTokenFromUserId().getUserId())
                        .build();
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(TokenCanceledEvent event){
        this.status = "CANCELLED";
    }

    @CommandHandler
    public TokenDTO handle(GenerateJwtTokenCommand command, @NonNull TokenManagerService service){
        UUID tokenRandomId = UUID.randomUUID();



        var map = service.generateJwtTokensMap(command.getTokenFromUserId().getUserId(), tokenRandomId.toString());
//        AggregateLifecycle.apply(service.handleTokenInfoEvent(
//                command.getTokenFromUserId().getUserId(),
//                map,
//                tokenRandomId.toString()
//        ));
        var event = TokenGeneratedEvent.builder()
                .tokenId(command.getTokenFromUserId())
                .kid(tokenRandomId.toString())
                .lastKid(this.tokenId)
                .jwk(map.get("public_key"))
                .build();
        AggregateLifecycle.apply(event);
        return service.makeTokenDTO(map, tokenRandomId.toString());
    }
    @CommandHandler
    public TokenDTO handle(RefreshJwtTokenCommand command, @NonNull TokenManagerService service){
        UUID tokenRandomId = UUID.randomUUID();
        var map = service.refreshToken(command.getRefreshToken(),tokenRandomId.toString());

        if(map.get("last_token_id") != this.tokenId){
            throw new IllegalArgumentException("Last token id is not equal to current token id");
        }
        var event = TokenGeneratedEvent.builder()
                .tokenId(command.getTokenFromUserId())
                .kid(tokenRandomId.toString())
                .lastKid(this.tokenId)
                .jwk(map.get("public_key"))
                .build();
        AggregateLifecycle.apply(event);
        return service.makeTokenDTO(map, tokenRandomId.toString());
    }
    @EventSourcingHandler
    public void on(TokenGeneratedEvent event){
        this.tokenId = event.getKid();
    }
}
