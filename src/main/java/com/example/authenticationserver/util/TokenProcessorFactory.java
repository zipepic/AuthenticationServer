package com.example.authenticationserver.util;

import com.example.authenticationserver.util.tokenenum.TokenTypes;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TokenProcessorFactory {

  private final SecretKeySpec secret;

  public TokenProcessorFactory(String secret) {
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    this.secret = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }

  public TokenProcessorContext generate(TokenTypes type) {
    switch (type) {
      case JWT:
        return new TokenProcessorContext(new JwtManager(secret));
      case JWK:
        return new TokenProcessorContext(new JwkManager());
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
  public TokenProcessorContext generate(int type){
    switch (type) {
      case 0:
        return new TokenProcessorContext(new JwtManager(secret));
      case 1:
        return new TokenProcessorContext(new JwkManager());
      default:
        throw new IllegalArgumentException("Unknown token type");
    }
  }
}



