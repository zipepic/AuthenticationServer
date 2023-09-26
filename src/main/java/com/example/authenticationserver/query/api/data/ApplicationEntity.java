package com.example.authenticationserver.query.api.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ApplicationEntity {
  @Id
  private String clientId;
  private String secret;
  private String code;
}
