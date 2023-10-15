package com.example.authenticationserver.command.api.test;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwksEntity {
  @Id
  private String keyId;

  @Lob
  private String jwksData;

}
