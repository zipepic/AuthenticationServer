package com.example.authenticationserver;
import com.project.core.events.user.JwkTokenInfoEvent;
import com.project.core.queries.user.FetchJwkSet;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import tokenlib.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import tokenlib.util.jwk.SimpleJWK;


@Configuration
@ManagedResource(description = "Token Processor Factory", objectName = "com.example.authenticationserver:type=TokenProcessorFactory")
public class TokenManagementConfig {

  @Value("${app.secret}")
  private String jwtSecret;
  @Value("${app.token-type}")
  private int tokenTypeInt;
  private final QueryGateway queryGateway;
  @Autowired
  public TokenManagementConfig(QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
  }

  @ManagedAttribute(description = "Set Token Type. 0 - JWT, 1 - JWK")
  public void setTokenTypeInt(int tokenTypeInt) {
    var context = new AnnotationConfigApplicationContext(TokenManagementConfig.class);
    if (tokenTypeInt < 0 || tokenTypeInt > 1)
      throw new IllegalArgumentException("Unknown token type");
    this.tokenTypeInt = tokenTypeInt;
  }

  @ManagedAttribute(description = "Get Token Type. 0 - JWT, 1 - JWK")
  public int getTokenTypeInt() {
    return tokenTypeInt;
  }

  @Bean
  public AbstractTokenFactory tokenProcessorFactory() {
    var factory = new TokenProcessorFactory(jwtSecret,queryGateway, (x->{

//      GenericQueryMessage<String, List<JWK>> query
//        = new GenericQueryMessage<>("fetchJwkSet",
//        "fetchJwkSet", ResponseTypes.multipleInstancesOf(JWK.class));
      FetchJwkSet query = FetchJwkSet.builder().build();

      return queryGateway.query(query,ResponseTypes.multipleInstancesOf(SimpleJWK.class)).join();
    }));
    factory.setEventClassForJwk(JwkTokenInfoEvent.class);
    factory.setEventClassForJwt(JwkTokenInfoEvent.class);
    return factory;
  }

  @Bean
  public TokenFacade tokenFacade(AbstractTokenFactory abstractTokenFactory) {
      return abstractTokenFactory.generate(tokenTypeInt);
  }
}
