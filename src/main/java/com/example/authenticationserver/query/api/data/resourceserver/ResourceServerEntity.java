package com.example.authenticationserver.query.api.data.resourceserver;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class ResourceServerEntity {
  @Id
  private String resourceServerName;
  private String secret;
  private Date createAt;
  private String status;
}
