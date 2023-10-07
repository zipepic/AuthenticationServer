package com.example.authenticationserver.query.api.event;

import com.project.core.events.code.AuthorizationCodeGeneratedEvent;
import com.project.core.events.code.AuthorizationCodeUsedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationCodeEventHandler {
  @EventHandler
  public void handle(AuthorizationCodeGeneratedEvent event) {

  }
  @EventHandler
  public void handle(AuthorizationCodeUsedEvent event) {

  }
}
