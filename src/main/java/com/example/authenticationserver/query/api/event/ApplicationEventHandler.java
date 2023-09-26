package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.ApplicationEntity;
import com.example.authenticationserver.query.api.data.ApplicationRepository;
import com.project.core.events.ApplicationCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventHandler {
  private final ApplicationRepository applicationRepository;
  @Autowired
  public ApplicationEventHandler(ApplicationRepository applicationRepository) {
    this.applicationRepository = applicationRepository;
  }

  @EventHandler
  public void on(ApplicationCreatedEvent event){
    ApplicationEntity applicationEntity = new ApplicationEntity();
    BeanUtils.copyProperties(event,applicationEntity);
    applicationRepository.save(applicationEntity);
  }
}
