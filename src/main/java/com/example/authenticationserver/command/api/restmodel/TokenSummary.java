package com.example.authenticationserver.command.api.restmodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenSummary {
  private String accessToken;
  private String refreshToken;
}
