package com.example.authenticationserver.util;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyPair;

public class KeyContainer {
  private final SecretKey secretKey;
  private final KeyPair keyPair;

  public KeyContainer(SecretKey secretKey) {
    this.keyPair = null;
    this.secretKey = secretKey;
  }
  public KeyContainer(KeyPair keyPair) {
    this.secretKey = null;
    this.keyPair = keyPair;
  }
  public Key getSignKey() {
    if(keyPair != null)
      return keyPair.getPrivate();

    return secretKey;
  }
  public Key getVerifyKey() {
    if(keyPair != null)
      return keyPair.getPublic();

    return secretKey;
  }
}
