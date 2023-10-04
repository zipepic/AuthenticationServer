package com.example.authenticationserver.query.api.data.token;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class TokenEntity {
  @Id
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  //TODO fix this(returning null)
  @OneToMany(mappedBy = "tokenEntity", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<AccessToken> accessTokens;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;
}
