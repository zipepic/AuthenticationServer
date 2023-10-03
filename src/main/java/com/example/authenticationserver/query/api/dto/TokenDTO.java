package com.example.authenticationserver.query.api.dto;

import jakarta.persistence.ElementCollection;
import lombok.Data;

import java.util.List;

@Data
public class TokenDTO {
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  private List<String> accessToken;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;
}
