package com.example.authenticationserver.command.api.restmodel.application;

import lombok.Data;

@Data
public class ApplicationCreateRestModel {
  private String clientId;
  private String secret;
}
