package com.example.authenticationserver.util.newutil.tokenenum;

public enum TokenFields {
  TOKEN_TYPE("token_type"),
  PUBLIC_KEY("public_key"),
  KID("kid");

  private final String value;

  TokenFields(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
