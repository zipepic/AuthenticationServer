package com.example.authenticationserver;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import tokenlib.util.TokenFacade;
import tokenlib.util.TokenProcessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;


@Configuration
@ManagedResource(description = "Token Processor Factory", objectName = "com.example.authenticationserver:type=TokenProcessorFactory")
public class AppConfig {

  @Value("${app.secret}")
  private String jwtSecret;
  @Value("${app.token-type}")
  private int tokenTypeInt;
  private final QueryGateway queryGateway;
  @Autowired
  public AppConfig(QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
  }

  @ManagedAttribute(description = "Set Token Type. 0 - JWT, 1 - JWK")
  public void setTokenTypeInt(int tokenTypeInt) {
    var context = new AnnotationConfigApplicationContext(AppConfig.class);
    if (tokenTypeInt < 0 || tokenTypeInt > 1)
      throw new IllegalArgumentException("Unknown token type");
    this.tokenTypeInt = tokenTypeInt;
  }

  @ManagedAttribute(description = "Get Token Type. 0 - JWT, 1 - JWK")
  public int getTokenTypeInt() {
    return tokenTypeInt;
  }

  @Bean
  public TokenProcessorFactory tokenProcessorFactory() {
    return new TokenProcessorFactory(jwtSecret,queryGateway);
  }

  @Bean
  public TokenFacade tokenFacade(TokenProcessorFactory tokenProcessorFactory) {
      return tokenProcessorFactory.generate(tokenTypeInt);
  }
}
