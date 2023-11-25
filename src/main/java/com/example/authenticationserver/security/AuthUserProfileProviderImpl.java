package com.example.authenticationserver.security;

import com.example.authenticationserver.security.service.UserDetailsLoader;
import com.example.authenticationserver.security.service.UserProfileDetailsLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthUserProfileProviderImpl implements AuthenticationProvider {
  private final UserDetailsLoader userDetailsLoader;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  public AuthUserProfileProviderImpl(UserDetailsLoader userDetailsLoader, PasswordEncoder passwordEncoder) {
    this.userDetailsLoader = userDetailsLoader;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userName = authentication.getName();

    UserDetails userDetails = userDetailsLoader.loadUserByUsername(userName);

    String password = authentication.getCredentials().toString();

    if(!passwordEncoder.matches(password, userDetails.getPassword())){
      throw new BadCredentialsException("Invalid password");
    }
    return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
