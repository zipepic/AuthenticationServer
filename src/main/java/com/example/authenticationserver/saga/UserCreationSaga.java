package com.example.authenticationserver.saga;

import com.project.core.commands.token.CancelTokenCreationCommand;
import com.project.core.commands.token.CreateTokenCommand;
import com.project.core.commands.user.CancelUserCreationCommand;
import com.project.core.commands.user.CompleteWereUserCreationCommand;
import com.project.core.dto.TokenId;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import com.project.core.events.user.UserCanceledCreationEvent;
import com.project.core.events.user.UserCreatedFromProviderIdEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import com.project.core.events.user.UserWereCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
@Slf4j
public class UserCreationSaga {
    @Autowired
    private transient CommandGateway commandGateway;
    @StartSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserCreatedFromProviderIdEvent event){
        log.info("Saga invoked by UserCreatedFromProviderIdEvent {}", event.getUserId());
        try {
        var command = CreateTokenCommand.builder()
                .tokenFromUserId(new TokenId(event.getUserId()))
                .providerId(event.getProviderId())
                .authProvider(event.getAuthProvider())
                .role(event.getRole())
                .build();

            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            cancelUserCommand(event);
            System.out.println(e.getMessage());;
        }
    }
    @StartSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserProfileCreatedEvent event){
        log.info("Saga invoked by UserCreatedFromProviderIdEvent {}", event.getUserId());
        try {
            var command = CreateTokenCommand.builder()
                    .tokenFromUserId(new TokenId(event.getUserId()))
                    .providerId(null)
                    .authProvider(null)
                    .role(event.getRole())
                    .build();

            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            cancelUserCommand(event);
            System.out.println(e.getMessage());;
        }
    }

    @SagaEventHandler(associationProperty = "userId")
    public void handle(TokenCreatedEvent event) {
        log.info("Saga invoked by TokenCreatedEvent {}", event.getTokenFromUserId());
        var command = CompleteWereUserCreationCommand.builder()
                .userId(event.getTokenFromUserId().getUserId())
                .status("COMPLETED")
                .build();
        try {
//            if (true)
//                throw new Exception("test");
            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            cancelTokenCreation(event);
            e.printStackTrace();
        }
    }
    @SagaEventHandler(associationProperty = "userId")
    public void handle(TokenCanceledEvent event) {
        log.info("Saga invoked by TokenCanceledEvent {}", event.getTokenFromUserId());
        cancelUserCommand(event);
    }
    private void cancelUserCommand(UserProfileCreatedEvent event) {
        var command = CancelUserCreationCommand.builder()
                .userId(event.getUserId())
                .build();
        commandGateway.sendAndWait(command);
    }
    private void cancelUserCommand(TokenCanceledEvent event) {
        var command = CancelUserCreationCommand.builder()
                .userId(event.getTokenFromUserId().getUserId())
                .build();
        commandGateway.sendAndWait(command);
    }
    private void cancelUserCommand(UserCreatedFromProviderIdEvent event) {
        var command = CancelUserCreationCommand.builder()
                .userId(event.getUserId())
                .build();
        commandGateway.sendAndWait(command);
    }

    private void cancelTokenCreation(TokenCreatedEvent event) {
        var command = CancelTokenCreationCommand.builder()
                .tokenFromUserId(event.getTokenFromUserId())
                .build();
        commandGateway.sendAndWait(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserWereCompletedEvent event) {
        log.info("Saga completed {}", event.getUserId());
    }
    @EndSaga
    @SagaEventHandler(associationProperty = "userId")
    public void handle(UserCanceledCreationEvent event) {
        log.info("Saga cancelled {}", event.getUserId());
    }
    public String tokenFromUserIdM(TokenId tokenId){
        return tokenId.getUserId();
    }
}
