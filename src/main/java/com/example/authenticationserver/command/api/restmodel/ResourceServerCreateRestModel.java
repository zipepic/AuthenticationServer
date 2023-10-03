package com.example.authenticationserver.command.api.restmodel;

import lombok.Data;

@Data
public class ResourceServerCreateRestModel {
  private String resourceServerName;
  private String secret;
}
