package com.example.authenticationserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Service
public class JwtManager extends TokenProcessor {
  private static SecretKeySpec secretKey;
  @Autowired
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
  public JwtBuilder tokenId(JwtBuilder iwt, String tokeId) {
    return iwt.setId(tokeId);
  }

  @Override
  public Claims tokenId(Claims claims, String tokenId) {
    return claims.setId(tokenId);
  }

  @Override
  public void save() throws IOException, ParseException {

  }
}
