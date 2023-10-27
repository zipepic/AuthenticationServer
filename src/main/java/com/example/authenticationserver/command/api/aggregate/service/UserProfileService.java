package com.example.authenticationserver.command.api.aggregate.service;

import com.example.authenticationserver.util.JwkManager;
import com.example.authenticationserver.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserProfileService {
  private final TokenUtils tokenUtils;
  @Autowired
  public UserProfileService(@Qualifier("jwtManager") TokenUtils tokenUtils) {
    this.tokenUtils = tokenUtils;
  }

  public Map<String, String> generateJwtTokens(String userId, String tokenId, String tokenType) throws Exception{
    if(tokenType.equals("JWK")) {
      return tokenUtils.generateJwtTokens(userId, tokenId);
    }else{
      throw new IllegalArgumentException("Invalid token type");
    }
  }
  public Map<String,String> refreshJwtToken(String refreshToken, String tokenId){
    try {
      var claims = tokenUtils.extractClaims(refreshToken);
      return tokenUtils.refresh(claims,tokenId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
