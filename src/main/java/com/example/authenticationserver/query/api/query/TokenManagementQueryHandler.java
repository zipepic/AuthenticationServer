package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.token.TokenEntity;
import com.example.authenticationserver.query.api.data.token.TokenRepository;
import com.example.authenticationserver.query.api.dto.TokenDTO;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.queries.FetchTokensByTokenId;
import com.project.core.queries.ValidateTokenQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenManagementQueryHandler {
  private final TokenRepository tokenRepository;
  @Autowired
  public TokenManagementQueryHandler(TokenRepository tokenRepository) {
    this.tokenRepository = tokenRepository;
  }

  @QueryHandler
  public TokenDTO on(FetchTokensByTokenId query){
   Optional<TokenEntity> tokenEntityOptional =
     tokenRepository.findById(query.getTokenId());
   if(tokenEntityOptional.isPresent()){
     TokenEntity tokenEntity = tokenEntityOptional.get();
     TokenDTO tokenDTO = new TokenDTO();
     BeanUtils.copyProperties(tokenEntity,tokenDTO);
     return tokenDTO;
   }else {
     throw new IllegalArgumentException("Token not found");
   }
  }

  @QueryHandler
  public boolean on(ValidateTokenQuery query){
    Optional<TokenEntity> tokenEntityOptional =
      tokenRepository.findById(query.getTokenId());
    if(tokenEntityOptional.isPresent()){
      TokenEntity tokenEntity = tokenEntityOptional.get();
      return tokenEntity.getStatus().equals("CREATED")
        && JwtTokenUtils.validateToken(tokenEntity.getRefreshToken());
    }else {
      throw new IllegalArgumentException("Token not found");
    }
  }

}
