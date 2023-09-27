package com.example.authenticationserver.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
//TODO (1) create the ability to generate different jwt tokens (content and validity period) (2) put the secret in properties

public class JwtTokenUtils {
  @Value("${app.secret}")
  private static String secret;
  private static final Key secretKey;

  static {
    secretKey = generateSecretKey();
  }

  public static String generateToken(String clientId, long expiration) {
    Map<String, Object> claims = new HashMap<>();
    String code = Jwts.builder()
      .setSubject(clientId)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expiration))//week
      .signWith(secretKey)
      .compact();
    return code;
  }

  private static Key generateSecretKey() {
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
}

