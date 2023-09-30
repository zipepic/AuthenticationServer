package com.example.authenticationserver.query.api.data.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class UserProfileEntity {
  @Id
  private String userId;
  private String userName;
  private String passwordHash;
  private String code;
  private String userStatus;
  private Date createdAt;
  private Date lastUpdatedAt;
  private Date deleteAt;
}
