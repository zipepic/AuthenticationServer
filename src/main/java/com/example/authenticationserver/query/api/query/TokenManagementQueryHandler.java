package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.ResourceServerEntity;
import com.example.authenticationserver.query.api.data.ResourceServerRepository;
import com.example.authenticationserver.query.api.data.TokenEntity;
import com.example.authenticationserver.query.api.data.TokenRepository;
import com.example.authenticationserver.query.api.dto.ResourceServerDTO;
import com.example.authenticationserver.query.api.dto.TokenDTO;
import com.project.core.queries.FetchResourceServersQuery;
import com.project.core.queries.FetchTokensByTokenId;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

}
