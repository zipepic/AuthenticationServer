package com.example.authenticationserver.util.newutils;

import com.example.authenticationserver.util.AppConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JwtManager extends TokenProcessor{
  private static Key secretKey;

  public JwtManager(SecretKeySpec secretKey) {
    this.secretKey = secretKey;
  }

  @Override
  public Claims extractClaims(String jwtToken) throws Exception {
    return Jwts.parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(jwtToken)
      .getBody();
  }

  @Override
  public String signAndCompactWithDefaults(JwtBuilder jwt) {
    return jwt
      .setIssuer(AppConstants.ISSUER.toString())
      .setIssuedAt(new Date())
      .signWith(secretKey).compact();
  }

  @Override
  public Map<String, String> refresh(Claims claims) throws NoSuchAlgorithmException, IOException, ParseException {
    String tokenId = UUID.randomUUID().toString();

    if(!isRefreshToken(claims))
      throw new IllegalArgumentException("Invalid token");

    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);

    refreshTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())).setId(tokenId);

    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);

    accessTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal()))
      .put("token_type", "access_token");

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateTokenWithClaims(refreshTokenClaims));
    tokenMap.put("access", generateTokenWithClaims(accessTokenClaims));

    return tokenMap;
  }
  @Override
  public JwtBuilder tokenId(JwtBuilder iwt) {
    return iwt.setId(UUID.randomUUID().toString());
  }
}
