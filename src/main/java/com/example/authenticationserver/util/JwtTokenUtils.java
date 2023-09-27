package com.example.authenticationserver.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;

public class JwtTokenUtils {

  private static final Key secretKey;
  private static final long expiration = 86400000 * 7; // 7 days

  static {
    secretKey = generateSecretKey();
  }

  public static String generateToken(String clientId) {
    Map<String, Object> claims = new HashMap<>();
    String code = Jwts.builder()
      .setSubject(clientId)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + 86400000 * 7))//week
      .signWith(secretKey)
      .compact();
    return code;
  }

  private static Key generateSecretKey() {
    byte[] secretKeyBytes = Base64.getDecoder().decode("aM7l+SXuEgqITZT5JI9uZK9IRcuIMk9ww2K1udnSk1U="); // Замените на ваш реальный секретный ключ
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
}

