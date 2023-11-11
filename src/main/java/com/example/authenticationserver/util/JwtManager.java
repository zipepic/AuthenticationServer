package com.example.authenticationserver.util;

import com.example.authenticationserver.util.jwk.AppConstants;
import com.nimbusds.jose.JOSEException;
import com.project.core.events.user.RefreshTokenForUserProfileGeneratedEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtManager implements JwtProvider{
  private final SecretKeySpec secretKey;
  public JwtManager(SecretKeySpec secretKey) {
    this.secretKey = secretKey;
  }
  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    return Jwts.parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(jwtToken)
      .getBody();
  }

  @Override
  public String generateSignedCompactToken(JwtBuilder jwt, String tokenId) {
    return jwt
      .setIssuer(AppConstants.ISSUER.toString())
      .setIssuedAt(new Date())
      .signWith(secretKey).compact();
  }

  @Override
  public Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId) throws NoSuchAlgorithmException {
    access.setId(tokenId);
    refresh.setId(tokenId);
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateSignedCompactToken(refresh,tokenId));
    tokenMap.put("access", generateSignedCompactToken(access,tokenId));
    return tokenMap;
  }
  @Override
  public Class<RefreshTokenForUserProfileGeneratedEvent> getEventClass() {
    return RefreshTokenForUserProfileGeneratedEvent.class;
  }
}
