package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.project.core.queries.FindUserIdByOneTimeCodeQuery;
import com.project.core.queries.FindUserIdByUserNameQuery;
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
  public String findUserIdByOneTimeCode(FindUserIdByOneTimeCodeQuery query) {
    Optional<UserProfileEntity> optionalUserProfileEntity =
      userProfileRepository.findByCode(query.getCode());

    if(optionalUserProfileEntity.isPresent()){
      UserProfileEntity userProfile = optionalUserProfileEntity.get();
      return userProfile.getUserId();
    }
    else {
      throw new IllegalArgumentException("User not found");
    }
  }

}
