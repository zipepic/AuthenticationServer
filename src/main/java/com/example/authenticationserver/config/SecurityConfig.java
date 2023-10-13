package com.example.authenticationserver.config;

import com.example.authenticationserver.config.filter.ErrorHandlingFilter;
import com.example.authenticationserver.config.filter.auth.LoadUserFromDatabaseFilterByJwt;
import com.example.authenticationserver.config.filter.auth.TokenSignatureVerificationFilter;
import com.example.authenticationserver.config.filter.token.JwtRefreshFilter;
import com.example.authenticationserver.config.filter.token.TokenGenerationFilter;
import com.example.authenticationserver.config.filter.token.TokenTypeFilter;
import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import com.example.authenticationserver.service.UserProfileDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final AuthUserProfileProviderImpl authUserProfileProvider;
  private final UserProfileDetailsService userProfileDetailsService;
  private final QueryGateway queryGateway;
  private final CommandGateway commandGateway;
  @Autowired
  public SecurityConfig(AuthUserProfileProviderImpl authUserProfileProvider, UserProfileDetailsService userProfileDetailsService, QueryGateway queryGateway, CommandGateway commandGateway) {
    this.authUserProfileProvider = authUserProfileProvider;
    this.userProfileDetailsService = userProfileDetailsService;
    this.queryGateway = queryGateway;
    this.commandGateway = commandGateway;
  }
  @Bean
  protected SecurityFilterChain filterChainAuth(HttpSecurity http) throws Exception {
    http.securityMatcher(new AntPathRequestMatcher("/auth/**"));

    configureDefault(http);

    configureAuthFilters(http);

    http.authenticationProvider(authUserProfileProvider);
    return http.build();
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(new AntPathRequestMatcher("/security/**"));

    configureDefault(http);

    configureAuthFilters(http);

    http.authenticationProvider(authUserProfileProvider);
    return http.build();
  }



  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
      .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
      .requestMatchers(new AntPathRequestMatcher("/auth/login"));
  }

  private void configureAuthFilters(HttpSecurity http) throws Exception {
    http
      .addFilterBefore(errorHandlingFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(new TokenSignatureVerificationFilter(), ErrorHandlingFilter.class)
      .addFilterAfter(new LoadUserFromDatabaseFilterByJwt(userProfileDetailsService), TokenSignatureVerificationFilter.class);
  }
  private void configureDefault(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .formLogin(formLogin -> formLogin.disable())
      .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

  TokenTypeFilter tokenTypeFilter(){
    return new TokenTypeFilter(jwtRefreshFilter());
  }
  JwtRefreshFilter jwtRefreshFilter(){
    return new JwtRefreshFilter(tokenGenerationFilter());
  }
  TokenGenerationFilter tokenGenerationFilter(){
    return new TokenGenerationFilter(commandGateway, new ObjectMapper());
  }
  ErrorHandlingFilter errorHandlingFilter(){
    return new ErrorHandlingFilter(new ObjectMapper());
  }
}

