package com.example.authenticationserver.command.api.restmodel.user;

import lombok.Data;

@Data
public class UserProfileRestModel {
  private String userName;
  private String password;
}
