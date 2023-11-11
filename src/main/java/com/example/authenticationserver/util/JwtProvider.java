package com.example.authenticationserver.util;

import io.jsonwebtoken.JwtBuilder;

interface JwtProvider extends TokenProcessingStrategy {
  String generateSignedCompactToken(JwtBuilder jwt, String tokenId);
}
