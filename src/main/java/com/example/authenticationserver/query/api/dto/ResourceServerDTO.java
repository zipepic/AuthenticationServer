package com.example.authenticationserver.query.api.dto;

import lombok.Data;

import java.util.Date;
@Data
public class ResourceServerDTO {
  private String resourceServerName;
  private String secret;
  private Date createAt;
  private String status;
}
