package com.example.authenticationserver.util.newutil;

import com.example.authenticationserver.util.jwk.AppConstants;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
@Service
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
  public void save(String userId) throws IOException, ParseException {

  }
}
