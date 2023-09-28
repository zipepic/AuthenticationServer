package com.example.authenticationserver.command.api.restmodel;

import lombok.Data;

@Data
public class ApplicationRegistrationRestModel {
  private String clientId;
  private String responseType;
  private String state;
  private String scope;
  private String redirectUrl;
}
