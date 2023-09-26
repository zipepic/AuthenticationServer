package com.example.authenticationserver.query.api.event;

import com.project.core.events.ApplicationCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventHandler {
  @EventHandler
  public void on(ApplicationCreatedEvent event){

  }
}
