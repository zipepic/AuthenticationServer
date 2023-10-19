package com.example.authenticationserver.dto;

public interface TokenDTO {
  String getAccessToken();
  Integer getExpiresIn();
  Integer getRefreshExpiresIn();
  String getRefreshToken();
  String getTokenType();
  String getTokenId();
}
