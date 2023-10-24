package com.example.authenticationserver.util;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class TokenProcessor implements TokenUtils {
  public boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get("token_type");
    return "refresh_token".equals(tokenType);
  }
  public JWSHeader getJwtHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader();
  }
  public Map<String, String> generateJwtTokens(String userId, String tokenId) throws Exception{
    var refresh = Jwts.builder()
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + 1000000)) // Срок действия 1 час
      .setIssuedAt(new Date())
      .addClaims(Map.of("token_type","refresh_token"));
      tokenId(refresh,tokenId);

    var access = Jwts.builder()
      .setSubject(userId)
      .setExpiration(new Date(System.currentTimeMillis() + 1000000)) // Срок действия 10 min
      .addClaims(Map.of("token_type","access_token"));
    tokenId(access,tokenId);

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", signAndCompactWithDefaults(refresh));
    tokenMap.put("access", signAndCompactWithDefaults(access));
    save();
    return tokenMap;
  }
  public String generateTokenWithClaims(Claims claims) throws Exception {
    var jwt = Jwts.builder()
      .setClaims(claims);
    return signAndCompactWithDefaults(jwt);
  }
  public Map<String, String> refresh(Claims claims,String tokenId) throws Exception {

    if(!isRefreshToken(claims))
      throw new IllegalArgumentException("Invalid token");

    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);

    refreshTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal()));
    tokenId(refreshTokenClaims,tokenId);

    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);

    accessTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal()))
      .put("token_type", "access_token");

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateTokenWithClaims(refreshTokenClaims));
    tokenMap.put("access", generateTokenWithClaims(accessTokenClaims));
    save();
    return tokenMap;
  }
  public abstract Claims extractClaims(String jwtToken) throws Exception;
  public abstract String signAndCompactWithDefaults(JwtBuilder jwt) throws Exception;
  public abstract JwtBuilder tokenId(JwtBuilder iwt,String tokenId);
  public abstract Claims tokenId(Claims claims, String tokenId);
  public abstract void save() throws IOException, ParseException;
}

