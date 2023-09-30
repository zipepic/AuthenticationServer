package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.application.ApplicationEntity;
import com.example.authenticationserver.query.api.data.application.ApplicationRepository;
import com.project.core.queries.CheckLoginDataQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationQueryHandler {
  private final ApplicationRepository applicationRepository;
  private final PasswordEncoder passwordEncoder;
  @Autowired
  public ApplicationQueryHandler(ApplicationRepository applicationRepository, PasswordEncoder passwordEncoder) {
    this.applicationRepository = applicationRepository;
    this.passwordEncoder = passwordEncoder;
  }
  @QueryHandler
  public boolean on(CheckLoginDataQuery query){
    Optional<ApplicationEntity> optionalApplicationEntity =
      applicationRepository.findById(query.getClientId());
    if(optionalApplicationEntity.isPresent())
    {
      ApplicationEntity applicationEntity = optionalApplicationEntity.get();
      return passwordEncoder.matches(query.getSecret(),applicationEntity.getSecret());
    }
    return false;
  }
}
