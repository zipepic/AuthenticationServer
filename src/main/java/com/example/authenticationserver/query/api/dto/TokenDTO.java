package com.example.authenticationserver.query.api.dto;

public interface TokenDTO {
  String getAccessToken();
  Integer getExpiresIn();
  Integer getRefreshExpiresIn();
  String getRefreshToken();
  String getTokenType();
}
