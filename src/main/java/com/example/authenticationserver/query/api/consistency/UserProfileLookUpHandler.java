package com.example.authenticationserver.query.api.consistency;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.project.core.events.user.UserProfileCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("user-profile-group")
public class UserProfileLookUpHandler {
  private final UserProfileLookupRepository userProfileRepository;
  @Autowired
  public UserProfileLookUpHandler(UserProfileLookupRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  @EventHandler
  public void on(UserProfileCreatedEvent event){
    UserProfileLookupEntity userProfileEntity =
      new UserProfileLookupEntity();

    BeanUtils.copyProperties(event,userProfileEntity);

    userProfileRepository.save(userProfileEntity);
  }
}
