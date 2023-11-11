package com.example.authenticationserver.util;

import io.jsonwebtoken.JwtBuilder;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

interface TokenProcessingStrategy extends TokenClaimsExtractor, EventClassProvider{
  Map<String,String> generateTokenPair(JwtBuilder access,JwtBuilder refresh,String tokenId)
    throws NoSuchAlgorithmException;
}
