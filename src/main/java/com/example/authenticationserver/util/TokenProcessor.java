package com.example.authenticationserver.util;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

abstract class TokenProcessor implements TokenUtils {
  protected KeyContainer keyContainer;
  protected String tokenId;
  public boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get("token_type");
    return "refresh_token".equals(tokenType);
  }
  public JWSHeader getJwtHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader();
  }
  public Map<String, String> generateJwtTokens(String userId, String tokenId) throws Exception{
    this.keyContainer = getKeyContainer();
    this.tokenId = tokenId;
    var refresh = Jwts.builder()
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + 100000)) // Срок действия 1 час
      .setIssuedAt(new Date())
      .addClaims(Map.of("token_type","refresh_token"));
      tokenId(refresh);

    var access = Jwts.builder()
      .setSubject(userId)
      .setExpiration(new Date(System.currentTimeMillis() + 100000)) // Срок действия 10 min
      .addClaims(Map.of("token_type","access_token"));
    tokenId(access);

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", signAndCompactWithDefaults(refresh));
    tokenMap.put("access", signAndCompactWithDefaults(access));
    save(userId);
    return tokenMap;
  }
  public String generateTokenWithClaims(Claims claims) throws Exception {
    var jwt = Jwts.builder()
      .setClaims(claims);
    return signAndCompactWithDefaults(jwt);
  }
  public Map<String, String> refresh(Claims claims,String tokenId) throws Exception {
    this.tokenId = tokenId;
    if(!isRefreshToken(claims))
      throw new IllegalArgumentException("Invalid token");

    Claims refreshTokenClaims = Jwts.claims();
    refreshTokenClaims.putAll(claims);

    refreshTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + 100000));
    tokenId(refreshTokenClaims);

    Claims accessTokenClaims = Jwts.claims();
    accessTokenClaims.putAll(claims);
    tokenId(accessTokenClaims);

    accessTokenClaims
      .setExpiration(new Date(System.currentTimeMillis() + 100000))
      .put("token_type", "access_token");

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateTokenWithClaims(refreshTokenClaims));
    tokenMap.put("access", generateTokenWithClaims(accessTokenClaims));
    save(claims.getSubject());
    return tokenMap;
  }
  public abstract Claims extractClaims(String jwtToken) throws Exception;
  public abstract String signAndCompactWithDefaults(JwtBuilder jwt) throws Exception;
  protected abstract JwtBuilder tokenId(JwtBuilder iwt);
  protected abstract Claims tokenId(Claims claims);
  public abstract void save(String userId) throws IOException, ParseException;
  protected abstract KeyContainer getKeyContainer() throws NoSuchAlgorithmException;
  public abstract String getTokenId(String jwtToken) throws Exception;
}

