package com.example.authenticationserver.command.api.aggregate;

import com.example.authenticationserver.util.newutils.JwkManager;
import com.example.authenticationserver.util.newutils.JwtManager;
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

  public Map<String, String> generateJwtTokens(String userId, String tokenId) throws Exception{
    return jwkManager.generateJwtTokens(userId, tokenId);
  }

}
