package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.CreateApplicationCommand;
import com.project.core.events.ApplicationCreatedEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
public class ApplicationAggregate {
  @AggregateIdentifier
  private String clientId;
  private String secret;
  private String code;

  public ApplicationAggregate() {
  }
  @CommandHandler
  public ApplicationAggregate(CreateApplicationCommand command){
    //TODO move it to a separate class JwtUtils
    byte[] secretKeyBytes =
      Base64.getDecoder().decode("aM7l+SXuEgqITZT5JI9uZK9IRcuIMk9ww2K1udnSk1U=");
    Key secretKey =
      new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    String code = Jwts.builder()
      .setSubject(command.getClientId())
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 7))//week
      .signWith(secretKey)
      .compact();
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
