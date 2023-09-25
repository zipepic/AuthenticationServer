package com.example.authenticationserver.command.api.aggregate;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;

@Aggregate
public class UserProfile {
  @AggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private Date createdAt;
  private Date lastUpdatedAt;
  private Date deleteAt;
}
