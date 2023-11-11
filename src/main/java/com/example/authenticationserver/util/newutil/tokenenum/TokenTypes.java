package com.example.authenticationserver.util.newutil.tokenenum;

public enum TokenTypes {
  ACCESS_TOKEN("access_token"),
  REFRESH_TOKEN("refresh_token"),
  JWK("jwk"),
  JWT("jwt");

  private final String value;

  TokenTypes(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

