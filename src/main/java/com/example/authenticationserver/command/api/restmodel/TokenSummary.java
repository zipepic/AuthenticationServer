package com.example.authenticationserver.command.api.restmodel;

import com.example.authenticationserver.query.api.dto.TokenDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenSummary implements TokenDTO {
  private String accessToken;
  private Integer expiresIn;
  private Integer refreshExpiresIn;
  private String refreshToken;
  private String tokenType;
  private String tokenId;
  private String not_before_policy;
  private String session_state;
  private String scope;
}
