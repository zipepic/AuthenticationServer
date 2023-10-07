package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.CreateResourceServerCommand;
import com.project.core.events.ResourceServerCreatedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
@NoArgsConstructor
public class ResourceServerAggregate {
  @AggregateIdentifier
  private String resourceServerName;
  private String secret;
  private Date createAt;
  private String status;
  @CommandHandler
  public ResourceServerAggregate(CreateResourceServerCommand command) {
    var event = ResourceServerCreatedEvent.builder()
      .resourceServerName(command.getResourceServerName())
      .secret(command.getSecret())
      .status("CREATED")
      .createAt(new Date())
      .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(ResourceServerCreatedEvent event){
    this.resourceServerName = event.getResourceServerName();
    this.secret = event.getSecret();
    this.createAt = event.getCreateAt();
    this.status = event.getStatus();
  }
}
