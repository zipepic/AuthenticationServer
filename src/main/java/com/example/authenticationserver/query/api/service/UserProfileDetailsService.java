package com.example.authenticationserver.query.api.service;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.example.authenticationserver.security.UserProfileDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileDetailsService implements UserDetailsService {
  private final UserProfileRepository userProfileRepository;

  public UserProfileDetailsService(UserProfileRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findByUserName(username);
    if (userProfileEntity.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }
    return new UserProfileDetails(userProfileEntity.get());
  }

  public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
    Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findById(userId);
    if (userProfileEntity.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }
    return new UserProfileDetails(userProfileEntity.get());
  }
}
