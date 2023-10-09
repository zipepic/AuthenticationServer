package com.example.authenticationserver.query.api.data.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class TokenEntity implements Serializable {
  @Id
  private String tokenId;
  private String userId;
  private String clientId;
  private String tokenType;
  @OneToMany(mappedBy = "tokenEntity", cascade = CascadeType.ALL)
  private List<AccessToken> accessTokens;
  private Integer expires_in;
  private Integer refresh_expires_in;
  private String refreshToken;
  private String scope;
  private String status;
}
