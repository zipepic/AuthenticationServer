package com.example.authenticationserver.command.api.aggregate.service;

import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.dto.TokenDTO;
import com.example.authenticationserver.dto.TokenSummary;
import com.example.authenticationserver.util.jwk.AppConstants;
import com.example.authenticationserver.util.newutil.TokenFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
  private final TokenFacade tokenFacade;
  @Autowired
  public UserProfileService(TokenFacade tokenFacade) {
    this.tokenFacade = tokenFacade;
  }

  public TokenDTO generateJwtTokens(String userId, String tokenId, String tokenType){
    try {
        var tokenMap = tokenFacade.issueUserTokens(userId, tokenId);
        return TokenSummary.builder()
          .accessToken(tokenMap.get("access"))
          .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
          .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
          .refreshToken(tokenMap.get("refresh"))
          .tokenType("Bearer")
          .tokenId(tokenId.toString())
          .build();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  public TokenDTO refreshJwtToken(String refreshToken, String tokenId){
    try {
      var claims = tokenFacade.extractClaimsFromToken(refreshToken);
      var tokenMap = tokenFacade.refreshTokens(claims,tokenId);
      return TokenAuthorizationCodeDTO.builder()
        .accessToken(tokenMap.get("access"))
        .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
        .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
        .refreshToken(tokenMap.get("refresh"))
        .tokenType("Bearer")
        .tokenId(tokenId)
        .build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
