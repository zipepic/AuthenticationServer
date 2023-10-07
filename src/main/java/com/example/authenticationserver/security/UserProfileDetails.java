package com.example.authenticationserver.security;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserProfileDetails implements UserDetails {
  private final UserProfileEntity userProfileEntity;

  public UserProfileDetails(UserProfileEntity userProfileEntity) {
    this.userProfileEntity = userProfileEntity;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }

  @Override
  public String getPassword() {
    return this.userProfileEntity.getPasswordHash();
  }

  @Override
  public String getUsername() {
    return this.userProfileEntity.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
  public UserProfileEntity getUserProfileEntity(){
    return this.userProfileEntity;
  }
}
