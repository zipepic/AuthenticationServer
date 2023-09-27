package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.CreateApplicationCommand;
import com.project.core.events.ApplicationCreatedEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Aggregate
@Slf4j
public class ApplicationAggregate {
  @AggregateIdentifier
  private String clientId;
  private String secret;
  private String code;

  public ApplicationAggregate() {
  }
  @CommandHandler
  public ApplicationAggregate(CreateApplicationCommand command){
    String code = JwtTokenUtils.generateToken(command.getClientId());

    log.info("Code -> {}", code);

    ApplicationCreatedEvent event =
      ApplicationCreatedEvent.builder()
        .clientId(command.getClientId())
        .secret(command.getSecret())
        .code(code)
        .build();

    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(ApplicationCreatedEvent event){
    this.clientId = event.getClientId();
    this.secret = event.getSecret();
  }
}
