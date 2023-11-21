package com.example.authenticationserver.query.api.consistency;

import com.example.authenticationserver.command.api.aggregate.AuthorizationCodeFlowAggregate;
import com.project.core.queries.user.UserProfileLookupQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserProfileLookupQueryHandler {
  private final UserProfileLookupRepository userProfileRepository;
  @Autowired
  public UserProfileLookupQueryHandler(UserProfileLookupRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }
  @QueryHandler
  public Optional<UserProfileLookupEntity> handle(UserProfileLookupQuery query){
    return userProfileRepository.findByUserIdOrUserName(query.getUserId(),query.getUserName());
  }
}
