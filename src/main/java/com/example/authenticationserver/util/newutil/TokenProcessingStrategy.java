package com.example.authenticationserver.util.newutil;

import com.example.authenticationserver.util.jwk.KeyContainer;
import io.jsonwebtoken.JwtBuilder;

import java.security.NoSuchAlgorithmException;

interface TokenProcessingStrategy extends TokenSaver, TokenClaimsExtractor{
  String generateSignedCompactToken(JwtBuilder jwt,String tokenId);

}
