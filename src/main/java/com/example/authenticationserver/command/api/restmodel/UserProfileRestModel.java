package com.example.authenticationserver.command.api.restmodel;

import lombok.Data;

@Data
public class UserProfileRestModel {
  private String userName;
  private String password;
}
