package com.example.authenticationserver.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

public class JwtTokenUtils {
  private static Key secretKey;

  public JwtTokenUtils(Key secretKey) {
    this.secretKey = secretKey;
  }

  public static String generateToken(String issuer, long expiration, HashMap<String, String> claims) {
    return Jwts.builder()
      .setIssuedAt(new Date())
      .setSubject("sub")
      .setIssuer(issuer)
      .setClaims(claims)
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(secretKey)
      .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}


