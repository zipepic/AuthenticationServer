package com.example.authenticationserver.util;
import com.project.core.commands.ResourceServerDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class JwtTokenUtils {
  private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public static String generateToken(String issuer, long expiration, HashMap<String, Object> claims) {
    var jwt = Jwts.builder()
      .setIssuedAt(new Date())
      .setIssuer(issuer)
      .addClaims(claims)
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(secretKey);
    return jwt.compact();
  }

  public static boolean validateToken(String token) {
    try {
      Jwts.parser()
        .setSigningKey(secretKey)
        .parse(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
  public static List<String> generateTokenForResourceServices(List<ResourceServerDTO> resourceServerDTOList){
    List<String> tokens = resourceServerDTOList.stream()
      .map(dto -> generateToken(dto.getResourceServerName(), 60000,new HashMap<>()))
      .collect(Collectors.toList());

    return tokens;
  }
}


