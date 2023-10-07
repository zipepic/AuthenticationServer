package com.example.authenticationserver.security;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserProfileDetails implements UserDetails {
  private final UserProfileEntity userProfileEntity;

  public UserProfileDetails(UserProfileEntity userProfileEntity) {
    this.userProfileEntity = userProfileEntity;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(this.userProfileEntity.getRole()));
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
}
