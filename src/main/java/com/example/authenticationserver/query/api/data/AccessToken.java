package com.example.authenticationserver.query.api.data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AccessToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String accessToken;

  @ManyToOne
  @JoinColumn(name = "token_entity_id")
  private TokenEntity tokenEntity;
}
