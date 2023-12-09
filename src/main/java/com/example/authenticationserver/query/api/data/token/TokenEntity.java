package com.example.authenticationserver.query.api.data.token;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TokenEntity {
    @Id
    private String userId;
    private String githubId;
    private String googleId;
    private String userRole;
    private String tokenId;
    private String status;
}
