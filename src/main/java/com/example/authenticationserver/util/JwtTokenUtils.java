package com.example.authenticationserver.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtTokenUtils {
  private final String secret;
  private final Key secretKey;

  public JwtTokenUtils(@NonNull @Value("${app.secret:#{null}}") String secret) {
    this.secret = secret;
    this.secretKey = generateSecretKey(secret);
  }

  public String generateToken(String clientId, long expiration, HashMap<String, String> claims) {
    String code = Jwts.builder()
      .setSubject(clientId)
      .setClaims(claims)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(secretKey)
      .compact();
    return code;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Key generateSecretKey(String secret) {
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
}


