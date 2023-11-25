package com.example.authenticationserver.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsLoader extends UserDetailsService {
  UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException;
}
