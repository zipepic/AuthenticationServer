package com.example.authenticationserver.command.api.aggregate.service;

import com.example.authenticationserver.util.JwkManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserProfileService {
  private final JwkManager jwkManager;
  @Autowired
  public UserProfileService(JwkManager jwkManager) {
    this.jwkManager = jwkManager;
  }

  public Map<String, String> generateJwtTokens(String userId, String tokenId, String tokenType) throws Exception{
    if(tokenType.equals("JWK")) {
      return jwkManager.generateJwtTokens(userId, tokenId);
    }else{
      throw new IllegalArgumentException("Invalid token type");
    }
  }

}