package com.example.authenticationserver.query.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenAuthorizationCodeDTO {
  private String accessToken;
  private Integer expiresIn;
  private Integer refreshExpiresIn;
  private String refreshToken;
  private String tokenType;
}
