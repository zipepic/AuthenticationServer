package com.example.authenticationserver.query.api.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class TokenEntity {
  @Id
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  private String accessToken;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;
}
