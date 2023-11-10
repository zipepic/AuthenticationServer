package com.example.authenticationserver.util.newutil;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class TokenProcessorFactory {
  private final String secret;

  // Конструктор для инъекции значения secret
  public TokenProcessorFactory(@Value("${jwt.secret}") String secret) {
    this.secret = secret;
  }

  public TokenProcessorContext generate() {
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    var sec =  new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
    return new TokenProcessorContext(new JwtManager(sec));
  }
}

