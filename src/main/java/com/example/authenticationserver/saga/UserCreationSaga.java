package com.example.authenticationserver.saga;

import com.project.core.commands.token.CancelTokenCreationCommand;
import com.project.core.commands.token.CreateTokenCommand;
import com.project.core.commands.user.CancelUserCreationCommand;
import com.project.core.commands.user.CompleteWereUserCreationCommand;
import com.project.core.commands.user.CreateUserFromProviderIdCommand;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import com.project.core.events.user.UserCanceledCreationEvent;
import com.project.core.events.user.UserCreatedFromProviderIdEvent;
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
        var command = CreateTokenCommand.builder()
                .userId(event.getUserId()+"token")
                .providerId(event.getProviderId())
                .authProvider(event.getAuthProvider())
                .role(event.getRole())
                .build();
        try {
            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SagaEventHandler(associationProperty = "userId")
    public void handle(TokenCreatedEvent event) {
        log.info("Saga invoked by TokenCreatedEvent {}", event.getUserId());
        var command = CompleteWereUserCreationCommand.builder()
                .userId(event.getUserId().replace("token",""))
                .status("COMPLETED")
                .build();
        try {
            commandGateway.sendAndWait(command);
        } catch (Exception e) {
            cancelTokenCreation(event);
            e.printStackTrace();
        }
    }
    @SagaEventHandler(associationProperty = "userId")
    public void handle(TokenCanceledEvent event) {
        log.info("Saga invoked by TokenCanceledEvent {}", event.getUserId());
        cancelUserCommand(event);
    }

    private void cancelUserCommand(TokenCanceledEvent event) {
        var command = CancelUserCreationCommand.builder()
                .userId(event.getUserId())
                .build();
        commandGateway.sendAndWait(command);
    }

    private void cancelTokenCreation(TokenCreatedEvent event) {
        var command = CancelTokenCreationCommand.builder()
                .userId(event.getUserId())
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

}
