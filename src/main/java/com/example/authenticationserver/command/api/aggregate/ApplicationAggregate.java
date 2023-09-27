package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.CreateApplicationCommand;
import com.project.core.commands.LoginApplicationCommand;
import com.project.core.commands.RegisterApplicationCommand;
import com.project.core.events.ApplicationCreatedEvent;
import com.project.core.events.ApplicationLoggedInEvent;
import com.project.core.events.ApplicationRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;


@Aggregate
@Slf4j
public class ApplicationAggregate {
  @AggregateIdentifier
  private String clientId;
  private String secret;
  private String code;
  private String refreshToken;

  public ApplicationAggregate() {
  }
  @CommandHandler
  public ApplicationAggregate(CreateApplicationCommand command){
    ApplicationCreatedEvent event =
      ApplicationCreatedEvent.builder()
        .clientId(command.getClientId())
        .secret(command.getSecret())
        .build();

    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(ApplicationCreatedEvent event){
    this.clientId = event.getClientId();
    this.secret = event.getSecret();
  }
  @CommandHandler
  public void handle(RegisterApplicationCommand command){
    String code = JwtTokenUtils.generateToken(command.getClientId());

    log.info(code);

    ApplicationRegisteredEvent event =
      ApplicationRegisteredEvent.builder()
        .clientId(command.getClientId())
        .code(code)
        .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(ApplicationRegisteredEvent event){
    this.code = event.getCode();
  }
  @CommandHandler
  public void handle(LoginApplicationCommand command){
  String refreshToken = JwtTokenUtils.generateToken(clientId);

  log.info("Refresh token -> {}", refreshToken);
    ApplicationLoggedInEvent event =
      ApplicationLoggedInEvent.builder()
        .clientId(command.getClientId())
        .refreshToken(refreshToken)
        .build();

    AggregateLifecycle.apply(refreshToken);
  }
  @EventSourcingHandler
  public void on(ApplicationLoggedInEvent event){
    this.refreshToken = event.getRefreshToken();
  }
}
