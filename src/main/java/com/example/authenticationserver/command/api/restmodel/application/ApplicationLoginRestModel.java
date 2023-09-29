package com.example.authenticationserver.command.api.restmodel.application;

import lombok.Data;

@Data
public class ApplicationLoginRestModel {
  private String clientId;
  private String secret;
  private String grantType;
  private String code;
  private String redirectUrl;
}
