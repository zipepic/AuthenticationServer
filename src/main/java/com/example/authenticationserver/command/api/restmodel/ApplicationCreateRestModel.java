package com.example.authenticationserver.command.api.restmodel;

import lombok.Data;

@Data
public class ApplicationCreateRestModel {
  private String clientId;
  private String secret;
}
