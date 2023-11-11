package com.example.authenticationserver.query.api.event;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.project.core.events.user.JwkTokenInfoEvent;
import com.project.core.events.user.RefreshTokenForUserProfileGeneratedEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  void on(RefreshTokenForUserProfileGeneratedEvent event){
    Optional<UserProfileEntity> userProfileEntityOptional =
      userProfileRepository.findById(event.getUserId());
    if(userProfileEntityOptional.isEmpty()){
      throw new RuntimeException("User not found");
    }
    UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
    userProfileEntity.setTokenId(event.getTokenId());
    userProfileRepository.save(userProfileEntity);
  }
  @EventHandler
  void handle(JwkTokenInfoEvent event) throws IOException, ParseException {
    ObjectMapper objectMapper = new ObjectMapper();
    JWKSet jwkSet = JWKSet.load(new File("/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json"));

    List<JWK> keys = new ArrayList<>(jwkSet.getKeys());

    keys = keys.stream().filter(key -> !key.getKeyID().equals(event.getKid())).collect(Collectors.toList());

    keys.add(RSAKey.parse(event.getPublicKey()));

    JWKSet updatedJWKSet = new JWKSet(keys);
    Optional<UserProfileEntity> userProfileEntityOptional =
      userProfileRepository.findById(event.getUserId());
    if(userProfileEntityOptional.isEmpty()){
      throw new RuntimeException("User not found");
    }
    UserProfileEntity userProfileEntity = userProfileEntityOptional.get();
    userProfileEntity.setTokenId(event.getKid());
    userProfileRepository.save(userProfileEntity);

    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File("/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json"), updatedJWKSet.toJSONObject());
  }

}
