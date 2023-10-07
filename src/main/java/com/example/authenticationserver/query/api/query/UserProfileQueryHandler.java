package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.project.core.queries.user.FetchUserProfileByUserIdQuery;
import com.project.core.queries.user.FetchUserProfileByUserNameQuery;
import com.project.core.queries.user.FindUserIdByUserNameQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProfileQueryHandler {
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
}
