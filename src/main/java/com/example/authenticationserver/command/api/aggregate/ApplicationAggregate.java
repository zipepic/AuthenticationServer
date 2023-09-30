package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.CreateApplicationCommand;
import com.project.core.commands.GenerateOneTimeCodeUserProfileCommand;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.UUID;

@Aggregate
@Slf4j
public class ApplicationAggregate {
  @AggregateIdentifier
  private String clientId;
  private String secret;
  private String refreshToken;

  @Autowired
  private JwtTokenUtils jwtTokenUtils;

  @Autowired
  private PasswordEncoder passwordEncoder;

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

  @CommandHandler
  public String handle(RegisterApplicationCommand command) {
    String code = jwtTokenUtils.generateToken(clientId, 60000, new HashMap<>());

    log.info("Code -> {}", code);

    ApplicationRegisteredEvent event = ApplicationRegisteredEvent.builder()
      .clientId(command.getClientId())
      .code(code)
      .build();
    AggregateLifecycle.apply(event);
    return code;
  }

  @EventSourcingHandler
  public void on(ApplicationRegisteredEvent event) {

  }

  @CommandHandler
  public TokenSummary handle(LoginApplicationCommand command) {
    String refreshToken = jwtTokenUtils.generateToken(clientId, 86400000 * 7, new HashMap<>());

    if (!jwtTokenUtils.validateToken(command.getCode())) {
      throw new IllegalArgumentException("Invalid code");
    }

    log.info("Refresh token -> {}", refreshToken);

    ApplicationLoggedInEvent event = ApplicationLoggedInEvent.builder()
      .clientId(command.getClientId())
      .refreshToken(refreshToken)
      .build();

    AggregateLifecycle.apply(event);
    return TokenSummary.builder()
      .access_token("access_token")
      .refresh_token(refreshToken)
      .build();
  }

  @EventSourcingHandler
  public void on(ApplicationLoggedInEvent event) {
    this.refreshToken = event.getRefreshToken();
  }

}
