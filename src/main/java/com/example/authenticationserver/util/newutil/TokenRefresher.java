package com.example.authenticationserver.util.newutil;

import io.jsonwebtoken.Claims;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

interface TokenRefresher {
  Map<String, String> refreshTokens(Claims claims, String tokenId) throws NoSuchAlgorithmException;
}

