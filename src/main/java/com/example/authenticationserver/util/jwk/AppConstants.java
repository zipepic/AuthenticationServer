package com.example.authenticationserver.util.jwk;

import org.springframework.beans.factory.annotation.Value;
public enum AppConstants {
  REFRESH_TOKEN_EXP_TIME,
  ACCESS_TOKEN_EXP_TIME,
  ISSUER;

  @Value("${app.refreshTokenExpTime}")
  private Integer refreshTokenExpTime;

  @Value("${app.accessTokenExpTime}")
  private Integer accessTokenExpTime;

  @Value("${app.issuer}")
  private String issuer;

}

