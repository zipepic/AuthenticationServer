package com.example.authenticationserver.util;
import com.example.authenticationserver.query.api.dto.TokenAuthorizationCodeDTO;
import com.project.core.commands.ResourceServerDTO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Slf4j
public class JwtTokenUtils {
  //TODO refracting this
  private static Key secretKey;
  private final static String ISSUER = "http://localhost:8080";

  public JwtTokenUtils(SecretKeySpec secretKey) {
    this.secretKey = secretKey;
  }

  public static String generateToken(String issuer, long expiration, HashMap<String, Object> claims, String subject) {
    var jwt = Jwts.builder()
      .setIssuedAt(new Date())
      .setIssuer(issuer)
      .addClaims(claims)
      .setSubject(subject) // user id
      .setExpiration(new Date(System.currentTimeMillis() + expiration));
      return signAndCompactWithDefaults(jwt);
  }
  public static String signAndCompactWithDefaults(JwtBuilder jwt) {
    return jwt
      .setIssuer(ISSUER)
      .setIssuedAt(new Date())
      .signWith(secretKey).compact();
  }
  public static Claims extractClaims(String token) throws IllegalArgumentException {
      return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
  }
  public static String generateTokenWithClaims(Claims claims){
    var jwt = Jwts.builder()
      .setClaims(claims);
      return signAndCompactWithDefaults(jwt);
  }
  public static TokenAuthorizationCodeDTO refresh(Claims claims) {

    UUID tokenId = UUID.randomUUID();

    if(!isRefreshToken(claims))
      throw new IllegalArgumentException("Invalid token");

    long expirationRefreshToken = Duration.between(claims.getIssuedAt().toInstant(),
        claims.getExpiration().toInstant())
      .toMillis();
    long exprirationAccessToken = expirationRefreshToken/10;

    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);

    refreshTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + expirationRefreshToken)).setId(tokenId.toString());

    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);

    accessTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + exprirationAccessToken))
      .put("token_type", "access_token");

    return TokenAuthorizationCodeDTO.builder()
      .accessToken(generateTokenWithClaims(accessTokenClaims))
      .expiresIn((int) exprirationAccessToken)
      .refreshExpiresIn((int) expirationRefreshToken)
      .refreshToken(generateTokenWithClaims(refreshTokenClaims))
      .tokenType(claims.get("type",String.class))
      .tokenId(tokenId.toString())
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
  public static void checkTokenSignature(String token) throws IllegalArgumentException{
    try {
      Jwts.parser()
        .setSigningKey(secretKey)
        .parse(token);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid token");
    }
  }
  public static List<String> generateTokenForResourceServices(List<ResourceServerDTO> resourceServerDTOList,String subject){
    List<String> tokens = resourceServerDTOList.stream()
      .map(dto -> generateToken(dto.getResourceServerName(), 60000,new HashMap<>(),subject))
      .collect(Collectors.toList());

    return tokens;
  }
  public static boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get("token_type");
    return "refresh_token".equals(tokenType);
  }
}


