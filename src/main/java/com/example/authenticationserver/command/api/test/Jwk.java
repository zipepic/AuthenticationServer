package com.example.authenticationserver.command.api.test;

import lombok.Data;

@Data
public class Jwk {
  private String kty;
  private String kid;
  private String e;
  private String n;
}
