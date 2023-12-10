package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.project.core.events.user.*;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tokenlib.util.jwk.AuthProvider;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ProcessingGroup("user-profile-group")
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
  public void handle(UserCreatedFromProviderIdEvent event){
    UserProfileEntity userProfileEntity =
      new UserProfileEntity();

    BeanUtils.copyProperties(event,userProfileEntity);
    if(event.getAuthProvider() == AuthProvider.GITHUB){
      userProfileEntity.setGithubId(event.getProviderId());
    }else if(event.getAuthProvider() == AuthProvider.GOOGLE){
        userProfileEntity.setGoogleId(event.getProviderId());
    }

    userProfileRepository.save(userProfileEntity);
  }
  @EventHandler
  void handle(UserProfileUpdatedEvent event){

  }
  @EventHandler
  void handle(UserProfilePasswordChangedEvent event){

  }
  @EventHandler
    void handle(ProviderIdBoundToUserEvent event){
        Optional<UserProfileEntity> userProfileEntityOptional =
            userProfileRepository.findById(event.getUserId());
        if(userProfileEntityOptional.isEmpty()){
            throw new RuntimeException("User not found");
        }
        UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
        if(event.getProviderType().equals("github")){
            userProfileEntity.setGithubId(event.getProviderId());
        }else if(event.getProviderType().equals("google")){
            userProfileEntity.setGoogleId(event.getProviderId());
        }
        userProfileRepository.save(userProfileEntity);
    }
    @EventHandler
    void handle(UserCanceledCreationEvent event){
    System.out.println("UserCanceledCreationEvent" + event.getUserId());
        userProfileRepository.deleteById(event.getUserId());
    }
    @EventHandler
    void handle(UserWereCompletedEvent event){
      System.out.println("UserWereCompletedEvent" + event.getUserId());
      userProfileRepository.findById(event.getUserId()).ifPresent(userProfileEntity -> {
        userProfileEntity.setUserStatus(event.getStatus());
        userProfileRepository.save(userProfileEntity);
      });
    }
}
