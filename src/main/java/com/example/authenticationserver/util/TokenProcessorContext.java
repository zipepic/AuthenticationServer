package com.example.authenticationserver.util;

import com.example.authenticationserver.util.jwk.AppConstants;
import com.example.authenticationserver.util.tokenenum.TokenExpiration;
import com.example.authenticationserver.util.tokenenum.TokenFields;
import com.example.authenticationserver.util.tokenenum.TokenTypes;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class TokenProcessorContext implements TokenOperationHandler, TokenFacade {
  private TokenProcessingStrategy tokenProcessingStrategy;
  public TokenProcessorContext(TokenProcessingStrategy tokenProcessingStrategy) {
    this.tokenProcessingStrategy = tokenProcessingStrategy;
  }
  private boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get(TokenFields.TOKEN_TYPE.getValue());
    return TokenTypes.REFRESH_TOKEN.getValue().equals(tokenType);
  }
  private JwtBuilder claimsToJwtBuilder(Claims claims){
    return Jwts.builder()
      .setClaims(claims);
  }
  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    return tokenProcessingStrategy.extractClaimsFromToken(jwtToken);
  }
  @Override
  public Map<String, String> refreshTokens(Claims claims, String tokenId) throws NoSuchAlgorithmException {
    if(!isRefreshToken(claims))
      throw new IllegalArgumentException("Token is not refresh token");

    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);

    refreshTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.ONE_HOUR.getMilliseconds()));

    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);

    accessTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.TEN_MINUTES.getMilliseconds()))
      .put(TokenFields.TOKEN_TYPE.getValue(), TokenTypes.ACCESS_TOKEN.getValue());

    return tokenProcessingStrategy.generateTokenPair(
      claimsToJwtBuilder(accessTokenClaims),
      claimsToJwtBuilder(refreshTokenClaims),
      tokenId);
  }

  @Override
  public Map<String, String> issueUserTokens(String userId, String tokenId) throws NoSuchAlgorithmException {
    var refresh = Jwts.builder()
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.ONE_HOUR.getMilliseconds())) // Срок действия 1 час
      .setIssuedAt(new Date())
      .addClaims(Map.of(TokenFields.TOKEN_TYPE.getValue(),TokenTypes.REFRESH_TOKEN.getValue()));

    var access = Jwts.builder()
      .setSubject(userId)
      .setExpiration(new Date(System.currentTimeMillis() + TokenExpiration.TEN_MINUTES.getMilliseconds())) // Срок действия 10 min
      .addClaims(Map.of(TokenFields.TOKEN_TYPE.getValue(), TokenTypes.ACCESS_TOKEN.getValue()));

    return tokenProcessingStrategy.generateTokenPair(access,refresh,tokenId);
  }

  @Override
  public Class<?> getEventClass() {
    return tokenProcessingStrategy.getEventClass();
  }
}
