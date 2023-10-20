package com.example.authenticationserver.util.newutils;

import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Map;

public abstract class TokenProcessor implements TokenUtils {
  public static boolean isRefreshToken(Claims claims) {
    String tokenType = (String) claims.get("token_type");
    return "refresh_token".equals(tokenType);
  }
  public static JWSHeader getJwtHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader();
  }
  public abstract Map<String, String> generateJwtTokens(String userId) throws Exception;
  public abstract Claims extractClaims(String jwtToken) throws Exception;
  public abstract String signAndCompactWithDefaults(JwtBuilder jwt) throws NoSuchAlgorithmException, IOException, ParseException;
  public abstract String generateTokenWithClaims(Claims claims) throws NoSuchAlgorithmException, IOException, ParseException;
  public abstract Map<String, String> refresh(Claims claims) throws NoSuchAlgorithmException, IOException, ParseException;
}

