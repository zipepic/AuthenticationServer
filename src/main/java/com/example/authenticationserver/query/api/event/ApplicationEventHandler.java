package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.application.ApplicationEntity;
import com.example.authenticationserver.query.api.data.application.ApplicationRepository;
import com.project.core.events.ApplicationCreatedEvent;
import com.project.core.events.ApplicationLoggedInEvent;
import com.project.core.events.ApplicationRegisteredEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationEventHandler {
  private final ApplicationRepository applicationRepository;

  @Autowired
  public ApplicationEventHandler(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @EventHandler
  public void on(ApplicationCreatedEvent event) {
    ApplicationEntity applicationEntity = new ApplicationEntity();
    BeanUtils.copyProperties(event, applicationEntity);
    applicationRepository.save(applicationEntity);
  }

  @EventHandler
  public void handle(ApplicationRegisteredEvent event) {
    Optional<ApplicationEntity> optionalApplicationEntity = applicationRepository.findById(event.getClientId());
    if (optionalApplicationEntity.isPresent()) {
      ApplicationEntity applicationEntity = optionalApplicationEntity.get();
      applicationEntity.setCode(event.getCode());
      applicationRepository.save(applicationEntity);
    } else {
      throw new IllegalArgumentException("Application not found");
    }
  }

  @EventHandler
  public void handle(ApplicationLoggedInEvent event) {
    Optional<ApplicationEntity> optionalApplicationEntity = applicationRepository.findById(event.getClientId());
    if (optionalApplicationEntity.isPresent()) {
      ApplicationEntity applicationEntity = optionalApplicationEntity.get();
      applicationEntity.setRefreshToken(event.getRefreshToken());
      applicationRepository.save(applicationEntity);
    } else {
      throw new IllegalArgumentException("Application not found");
    }
  }
}
