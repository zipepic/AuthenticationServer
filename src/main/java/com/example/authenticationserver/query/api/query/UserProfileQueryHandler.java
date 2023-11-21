package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.project.core.queries.user.*;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tokenlib.util.jwk.SimpleJWK;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserProfileQueryHandler {
  @Value("${app.jwk-file-path}")
  private String filePath;
  private final UserProfileRepository userProfileRepository;
  @Autowired
  public UserProfileQueryHandler(UserProfileRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }
  @QueryHandler
  public String findUserIdByUserName(FindUserIdByUserNameQuery query) {
    Optional<UserProfileEntity> optionalUserProfileEntity =
      userProfileRepository.findByUserName(query.getUserName());

    if(optionalUserProfileEntity.isPresent()){
      UserProfileEntity userProfile = optionalUserProfileEntity.get();
      return userProfile.getUserId();
    }
    else {
      throw new IllegalArgumentException("User not found");
    }
  }
  @QueryHandler
  public UserProfileEntity findUserProfileByUserId(FetchUserProfileByUserIdQuery query) {
    Optional<UserProfileEntity> optionalUserProfileEntity =
      userProfileRepository.findById(query.getUserId());

    if(optionalUserProfileEntity.isPresent()){
      return optionalUserProfileEntity.get();
    }
    else {
      throw new IllegalArgumentException("User not found");
    }
  }
  @QueryHandler
  public UserProfileEntity findUserProfileByUserName(FetchUserProfileByUserNameQuery query) {
    Optional<UserProfileEntity> optionalUserProfileEntity =
      userProfileRepository.findByUserName(query.getUserName());

    if(optionalUserProfileEntity.isPresent()){
      return optionalUserProfileEntity.get();
    }
    else {
      throw new IllegalArgumentException("User not found");
    }
  }
  @QueryHandler
  public boolean validateRefreshToken(ValidateRefreshTokenForUserProfileQuery query) {
    Optional<UserProfileEntity> optionalUserProfileEntity =
      userProfileRepository.findById(query.getUserId());

    if (optionalUserProfileEntity.isEmpty())
      throw new IllegalArgumentException("User not found");

    UserProfileEntity userProfileEntity = optionalUserProfileEntity.get();
    return userProfileEntity.getTokenId().equals(query.getTokenId());
  }
  @QueryHandler
  public List<SimpleJWK> fetchJwkSet(FetchJwkSet query) throws IOException, ParseException {

    var jwkSet = JWKSet.load(new File(filePath))
      .getKeys().stream().map(x->
      {
        try {
          return SimpleJWK.parse(x.toString());
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }).collect(Collectors.toList());

    return jwkSet;
  }
}
