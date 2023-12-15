package com.example.authenticationserver.query.api.data.jwk;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
@Entity
public class JwkEntity {
    @Id
    private String kid;
    @Lob
    private String publicKey;
}
