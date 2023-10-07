package com.example.authenticationserver.query.api.data.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
  @Override
  public String toString() {
    return "AccessToken{" +
      "id=" + id +
      ", accessToken='" + accessToken + '\'' +
      '}';
  }
}
