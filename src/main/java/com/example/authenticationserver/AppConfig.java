package com.example.authenticationserver;

import com.example.authenticationserver.util.newutil.TokenFacade;
import com.example.authenticationserver.util.newutil.TokenProcessorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Value("${app.secret}")
  private String jwtSecret;

  @Bean
  public TokenProcessorFactory tokenProcessorFactory() {
    return new TokenProcessorFactory(jwtSecret);
  }

  @Bean
  public TokenFacade tokenFacade(TokenProcessorFactory tokenProcessorFactory) {
    return tokenProcessorFactory.generate();
  }
}
