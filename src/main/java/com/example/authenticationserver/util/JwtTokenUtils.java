package com.example.authenticationserver.util;
import com.example.authenticationserver.query.api.dto.TokenAuthorizationCodeDTO;
import com.project.core.commands.ResourceServerDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class JwtTokenUtils {
  //TODO refracting this
  private static Key secretKey;

  public JwtTokenUtils(SecretKeySpec secretKey) {
    this.secretKey = secretKey;
  }

  public static String generateToken(String issuer, long expiration, HashMap<String, Object> claims, String subject) {
    var jwt = Jwts.builder()
      .setIssuedAt(new Date())
      .setIssuer(issuer)
      .addClaims(claims)
      .setSubject(subject) // user id
      .setExpiration(new Date(System.currentTimeMillis() + expiration))
      .signWith(secretKey);
    return jwt.compact();
  }
  public static String lightGenerateToken(JwtBuilder jwt) {
    return jwt.signWith(secretKey).compact();
  }
  public static Claims extractClaims(String token) {
    try {
      return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
    } catch (JwtException e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }
  public static String lightGenerateTokenWithClaims(Claims claims){
    return Jwts.builder()
      .setClaims(claims)
      .signWith(secretKey)
      .compact();
  }
  public static TokenAuthorizationCodeDTO refresh(String token) {
    Claims claims = extractClaims(token);
    long expirationRefreshToken = Duration.between(claims.getIssuedAt().toInstant(),
        claims.getExpiration().toInstant())
      .toMillis();
    long exprirationAccessToken = expirationRefreshToken/10;
    claims.setIssuedAt(new Date());
    Claims refreshTokenClaims = claims
      .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken));
    Claims accessTokenClaims = claims
      .setExpiration(new Date(System.currentTimeMillis() + exprirationAccessToken));
    return TokenAuthorizationCodeDTO.builder()
      .accessToken(lightGenerateTokenWithClaims(accessTokenClaims))
      .expiresIn((int) exprirationAccessToken)
      .refreshExpiresIn((int) expirationRefreshToken)
      .refreshToken(lightGenerateTokenWithClaims(refreshTokenClaims))
      .tokenType(claims.get("type",String.class))
      .build();
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
  public static String getUserName(String token){
    return Jwts.parser()
      .setSigningKey(secretKey)
      .parseClaimsJws(token)
      .getBody()
      .getSubject();
  }
  public static List<String> generateTokenForResourceServices(List<ResourceServerDTO> resourceServerDTOList,String subject){
    List<String> tokens = resourceServerDTOList.stream()
      .map(dto -> generateToken(dto.getResourceServerName(), 60000,new HashMap<>(),subject))
      .collect(Collectors.toList());

    return tokens;
  }

}


