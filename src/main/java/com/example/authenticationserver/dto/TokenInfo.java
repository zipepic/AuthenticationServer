package com.example.authenticationserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo {
  private String userId;
  private String clientId;
  private String scope;
}
