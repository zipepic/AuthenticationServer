package com.example.authenticationserver.util.newutil;

import com.example.authenticationserver.util.jwk.AppConstants;
import com.example.authenticationserver.util.jwk.KeyContainer;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenProcessorContext implements TokenOperationHandler, TokenFacade {
  private TokenProcessingStrategy tokenProcessingStrategy;
  public TokenProcessorContext(TokenProcessingStrategy tokenProcessingStrategy) {
    this.tokenProcessingStrategy = tokenProcessingStrategy;
  }
  public boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get("token_type");
    return "refresh_token".equals(tokenType);
  }

  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    return tokenProcessingStrategy.extractClaimsFromToken(jwtToken);
  }
  public String generateTokenWithClaims(Claims claims,String tokenId){
    var jwt = Jwts.builder()
      .setClaims(claims);
    return tokenProcessingStrategy.generateSignedCompactToken(jwt,tokenId);
  }

  @Override
  public Map<String, String> refreshTokens(Claims claims, String tokenId) {
    if(!isRefreshToken(claims))
      throw new IllegalArgumentException("Token is not refresh token");

    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);

    refreshTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + 100000));

    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);

    accessTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + 100000))
      .put("token_type", "access_token");

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateTokenWithClaims(refreshTokenClaims,tokenId));
    tokenMap.put("access", generateTokenWithClaims(accessTokenClaims,tokenId));
    return tokenMap;
  }

  @Override
  public void save(String userId) throws IOException, ParseException {
    tokenProcessingStrategy.save(userId);
  }

  @Override
  public Map<String, String> issueUserTokens(String userId, String tokenId) {
    var refresh = Jwts.builder()
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + 100000)) // Срок действия 1 час
      .setIssuedAt(new Date())
      .addClaims(Map.of("token_type","refresh_token"));

    var access = Jwts.builder()
      .setSubject(userId)
      .setExpiration(new Date(System.currentTimeMillis() + 100000)) // Срок действия 10 min
      .addClaims(Map.of("token_type","access_token"));

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", tokenProcessingStrategy.generateSignedCompactToken(refresh,tokenId));
    tokenMap.put("access", tokenProcessingStrategy.generateSignedCompactToken(access,tokenId));
    return tokenMap;
  }
}
