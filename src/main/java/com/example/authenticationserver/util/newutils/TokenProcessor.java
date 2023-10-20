package com.example.authenticationserver.util.newutils;

import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.util.AppConstants;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
  public Map<String, String> generateJwtTokens(String userId) throws Exception{
    var refresh = Jwts.builder()
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())) // Срок действия 1 час
      .setIssuedAt(new Date())
      .addClaims(Map.of("token_type","refresh_token"));
      tokenId(refresh);

    var access = Jwts.builder()
      .setSubject(userId)
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())) // Срок действия 10 min
      .addClaims(Map.of("token_type","access_token"));

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", signAndCompactWithDefaults(refresh));
    tokenMap.put("access", signAndCompactWithDefaults(access));
    return tokenMap;
  }
  public String generateTokenWithClaims(Claims claims) throws NoSuchAlgorithmException, IOException, ParseException{
    var jwt = Jwts.builder()
      .setClaims(claims);
    return signAndCompactWithDefaults(jwt);
  }
  public abstract Claims extractClaims(String jwtToken) throws Exception;
  public abstract String signAndCompactWithDefaults(JwtBuilder jwt) throws NoSuchAlgorithmException, IOException, ParseException;
  public abstract Map<String, String> refresh(Claims claims) throws NoSuchAlgorithmException, IOException, ParseException;
  public abstract JwtBuilder tokenId(JwtBuilder iwt);
}

