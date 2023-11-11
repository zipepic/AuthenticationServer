package com.example.authenticationserver.util.newutil;

import com.example.authenticationserver.util.jwk.AppConstants;
import com.example.authenticationserver.util.jwk.KeyContainer;
import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.JwtBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

interface JwkProvider extends TokenProcessingStrategy{
  KeyContainer obtainKeyContainer() throws NoSuchAlgorithmException;
  JWKSet loadJwkSetFromSource() throws IOException, ParseException;
  String extractKidFromTokenHeader(String jwtToken) throws ParseException;
  String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyContainer keyContainer);
}
