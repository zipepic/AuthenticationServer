package com.example.authenticationserver.security.config;

import com.example.authenticationserver.security.filter.ErrorHandlingFilter;
import com.example.authenticationserver.security.filter.auth.JWKsSignatureVerificationFilter;
import com.example.authenticationserver.security.filter.auth.LoadUserFromDatabaseFilterByJwt;
import com.example.authenticationserver.security.filter.auth.TokenRemoverFilter;
import com.example.authenticationserver.security.filter.token.JwtRefreshFilter;
import com.example.authenticationserver.security.filter.token.TokenGenerationFilter;
import com.example.authenticationserver.security.filter.URIFilter;
import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import com.example.authenticationserver.security.service.UserProfileDetailsService;
import com.example.authenticationserver.util.JwkManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final AuthUserProfileProviderImpl authUserProfileProvider;
  private final UserProfileDetailsService userProfileDetailsService;
  private final QueryGateway queryGateway;
  private final CommandGateway commandGateway;
  private final JwkManager jwkManager;
  @Autowired
  public SecurityConfig(AuthUserProfileProviderImpl authUserProfileProvider, UserProfileDetailsService userProfileDetailsService, QueryGateway queryGateway, CommandGateway commandGateway, JwkManager jwkManager) {
    this.authUserProfileProvider = authUserProfileProvider;
    this.userProfileDetailsService = userProfileDetailsService;
    this.queryGateway = queryGateway;
    this.commandGateway = commandGateway;
    this.jwkManager = jwkManager;
  }
  @Bean
  protected SecurityFilterChain filterChainAuth(HttpSecurity http) throws Exception {
    http.securityMatcher(new AntPathRequestMatcher("/auth/**"));

    configureDefault(http);

    configureAuthFilters(http);

    http.addFilterAfter(tokenTypeFilter(), LoadUserFromDatabaseFilterByJwt.class);

    http.authenticationProvider(authUserProfileProvider);
    return http.build();
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(new AntPathRequestMatcher("/login"));
    http.securityMatcher(new AntPathRequestMatcher("/registration"));

    configureDefault(http);
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
      .addFilterBefore(new ErrorHandlingFilter(new ObjectMapper()), UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(new TokenRemoverFilter(), ErrorHandlingFilter.class)
      .addFilterAfter(new JWKsSignatureVerificationFilter(jwkManager), TokenRemoverFilter.class)
      .addFilterAfter(new LoadUserFromDatabaseFilterByJwt(userProfileDetailsService), JWKsSignatureVerificationFilter.class);
  }
  private void configureDefault(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .formLogin(formLogin -> formLogin.disable())
      .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }

  URIFilter tokenTypeFilter(){
    return new URIFilter(jwtRefreshFilter());
  }
  JwtRefreshFilter jwtRefreshFilter(){
    return new JwtRefreshFilter(tokenGenerationFilter());
  }
  TokenGenerationFilter tokenGenerationFilter() { return new TokenGenerationFilter(commandGateway, new ObjectMapper());}

}

