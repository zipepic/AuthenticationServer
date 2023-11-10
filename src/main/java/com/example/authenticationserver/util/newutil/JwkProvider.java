package com.example.authenticationserver.util.newutil;

import com.example.authenticationserver.util.jwk.KeyContainer;
import com.nimbusds.jose.jwk.JWKSet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public interface JwkProvider extends TokenProcessingStrategy{
  KeyContainer obtainKeyContainer() throws NoSuchAlgorithmException;
  JWKSet loadJwkSetFromSource() throws IOException, ParseException;
  String extractKidFromTokenHeader(String jwtToken) throws ParseException;
}
