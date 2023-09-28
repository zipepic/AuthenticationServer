package com.example.authenticationserver.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

public class JwtTokenUtils {
  @Value("${app.secret:#{null}}")
  private static String secret;
  private static final Key secretKey;

  static {
    secretKey = generateSecretKey();
  }

  public static String generateToken(String clientId, long expiration, HashMap<String,String> claims) {
    String code = Jwts.builder()
      .setSubject(clientId)
      .setClaims(claims)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration))//week
      .signWith(secretKey)
      .compact();
    return code;
  }

  private static Key generateSecretKey() {
    if(secret == null){
      throw new IllegalArgumentException("Secret is null");
    }
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
}

