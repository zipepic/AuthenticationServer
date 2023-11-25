package com.example.authenticationserver.security.service;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.security.UserProfileDetails;
import com.project.core.queries.user.FetchUserProfileByUserIdQuery;
import com.project.core.queries.user.FetchUserProfileByUserNameQuery;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileDetailsLoaderService implements UserDetailsLoader {
  private final QueryGateway queryGateway;
  @Autowired
  public UserProfileDetailsLoaderService(QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      var query = FetchUserProfileByUserNameQuery.builder()
        .userName(username)
        .build();
      var userProfileEntity = queryGateway.query(query, UserProfileEntity.class).join();
      return new UserProfileDetails(userProfileEntity);
    } catch (Exception e) {
      throw new UsernameNotFoundException("User not found");
    }
  }
  @Override
  public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
    try {
      var query = FetchUserProfileByUserIdQuery.builder()
        .userId(userId)
        .build();
      var userProfileEntity = queryGateway.query(query, UserProfileEntity.class).join();
      return new UserProfileDetails(userProfileEntity);
    } catch (Exception e) {
      throw new UsernameNotFoundException("User not found");
    }
  }
}
