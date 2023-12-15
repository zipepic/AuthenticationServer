package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.jwk.JwkEntity;
import com.example.authenticationserver.query.api.data.jwk.JwkRepository;
import com.project.core.dto.JwksDTO;
import com.project.core.queries.FetchJwksQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenManagerQueryHandler {
    private final JwkRepository jwkRepository;

    public TokenManagerQueryHandler(JwkRepository jwkRepository) {
        this.jwkRepository = jwkRepository;
    }
    @QueryHandler
    public List<String> handle(FetchJwksQuery query) {
        return  jwkRepository.findAll().stream().map(JwkEntity::getPublicKey).filter(element -> element != null).toList();
    }
}
