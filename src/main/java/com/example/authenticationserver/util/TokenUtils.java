package com.example.authenticationserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Map;

public interface TokenUtils {
  public Map<String, String> generateJwtTokens(String userId, String tokenId) throws Exception;
  public Claims extractClaims(String jwtToken) throws Exception;
  public Map<String, String> refresh(Claims claims, String tokenId) throws Exception;
}
