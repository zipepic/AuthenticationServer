package com.example.authenticationserver.dto.application;

import lombok.Data;

@Data
public class ApplicationCreateRestModel {
  private String clientId;
  private String secret;
}
