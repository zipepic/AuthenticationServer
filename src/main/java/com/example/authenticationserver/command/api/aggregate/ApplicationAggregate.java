package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.app.CreateApplicationCommand;
import com.project.core.events.app.ApplicationCreatedEvent;
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

  public ApplicationAggregate() {
  }

  @CommandHandler
  public ApplicationAggregate(CreateApplicationCommand command) {
    ApplicationCreatedEvent event = ApplicationCreatedEvent.builder()
      .clientId(command.getClientId())
      .secret(command.getSecret())
      .build();

    AggregateLifecycle.apply(event);
  }

  @EventSourcingHandler
  public void on(ApplicationCreatedEvent event) {
    this.clientId = event.getClientId();
    this.secret = event.getSecret();
  }
}
