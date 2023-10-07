package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.resourceserver.ResourceServerEntity;
import com.example.authenticationserver.query.api.data.resourceserver.ResourceServerRepository;
import com.project.core.events.ResourceServerCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceServerEventHandler {
  private final ResourceServerRepository resourceServerRepository;
  @Autowired
  public ResourceServerEventHandler(ResourceServerRepository resourceServerRepository) {
    this.resourceServerRepository = resourceServerRepository;
  }

  @EventHandler
  public void handle(ResourceServerCreatedEvent event){
    var entity = new ResourceServerEntity();
    BeanUtils.copyProperties(event,entity);
    resourceServerRepository.save(entity);
  }
}
