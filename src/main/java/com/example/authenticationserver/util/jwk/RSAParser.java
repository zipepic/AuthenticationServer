package com.example.authenticationserver.util.jwk;

import com.example.authenticationserver.util.jwk.Jwk;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;

public class RSAParser {
  public static RSAKey parseRSAKeyFromJson(String json) throws JsonProcessingException {

      ObjectMapper objectMapper = new ObjectMapper();
      var jwk = objectMapper.readValue(json, Jwk.class);

      RSAKey rsaKey = new RSAKey.Builder(
        new Base64URL(jwk.getN()),
        new Base64URL(jwk.getE()))
        .keyID(jwk.getKid())
        .build();
      return rsaKey;

  }
}



