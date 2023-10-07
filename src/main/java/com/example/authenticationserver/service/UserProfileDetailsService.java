package com.example.authenticationserver.service;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.example.authenticationserver.security.UserProfileDetails;
import com.project.core.queries.user.FetchUserProfileByUserIdQuery;
import com.project.core.queries.user.FetchUserProfileByUserNameQuery;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileDetailsService implements UserDetailsService {
  private final QueryGateway queryGateway;
  @Autowired
  public UserProfileDetailsService(QueryGateway queryGateway) {
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
