package com.example.authenticationserver.command.api.restmodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenSummary {
  private String access_token;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refresh_token;
  private String token_type;
  private String id_token;
  private String not_before_policy;
  private String session_state;
  private String scope;
}
