package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.project.core.events.user.RefreshTokenForUserProfileGeneratedEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
  @EventHandler void on(RefreshTokenForUserProfileGeneratedEvent event){
    Optional<UserProfileEntity> userProfileEntityOptional =
      userProfileRepository.findById(event.getUserId());
    if(userProfileEntityOptional.isEmpty()){
      throw new RuntimeException("User not found");
    }
    UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
    userProfileEntity.setRefreshToken(event.getRefreshToken());
    userProfileRepository.save(userProfileEntity);
  }
}
