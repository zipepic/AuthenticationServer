package com.example.authenticationserver.query.api.consistency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserProfileLookupEntity {
  @Id
  private String userId;
  private String userName;
}
