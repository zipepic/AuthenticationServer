package com.example.authenticationserver.util.newutil;

import com.example.authenticationserver.util.jwk.KeyContainer;
import com.project.core.events.user.JwkTokenInfoEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

interface TokenProcessingStrategy extends TokenClaimsExtractor, EventClassProvider{
  String generateSignedCompactToken(JwtBuilder jwt,String tokenId);
  Map<String,String> generateTokenPair(JwtBuilder access,JwtBuilder refresh,String tokenId)
    throws NoSuchAlgorithmException;
}
