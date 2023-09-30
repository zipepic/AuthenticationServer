package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.project.core.events.OneTimeCodeUserProfileGeneratedEvent;
import com.project.core.events.UserProfileCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfileEventHandler {
  private final UserProfileRepository userProfileRepository;
  @Autowired
  public UserProfileEventHandler(UserProfileRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  @EventHandler
  public void on(UserProfileCreatedEvent event){
    UserProfileEntity userProfileEntity =
      new UserProfileEntity();

    BeanUtils.copyProperties(event,userProfileEntity);

    userProfileRepository.save(userProfileEntity);
  }
  @EventHandler
  public void on(OneTimeCodeUserProfileGeneratedEvent event){
    userProfileRepository.findById(event.getUserId())
      .ifPresent(userProfileEntity -> {
        userProfileEntity.setCode(event.getCode());
        userProfileRepository.save(userProfileEntity);
      });
  }
}
