package com.example.authenticationserver.security.config;

import com.example.authenticationserver.security.filter.ErrorHandlingFilter;
import com.example.authenticationserver.security.filter.auth.SignatureVerificationFilter;
import com.example.authenticationserver.security.filter.auth.LoadUserFromDatabaseFilterByJwt;
import com.example.authenticationserver.security.filter.auth.TokenRemoverFilter;
import com.example.authenticationserver.security.filter.token.JwtRefreshFilter;
import com.example.authenticationserver.security.filter.token.TokenGenerationFilter;
import com.example.authenticationserver.security.filter.URIFilter;
import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import com.example.authenticationserver.security.service.UserDetailsLoader;
import tokenlib.util.TokenFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Security configuration for the application.
 *
 * @author Konstantin Kokoshnikov
 * @see AuthUserProfileProviderImpl
 * @see UserDetailsLoader
 * @see QueryGateway
 * @see CommandGateway
 * @see TokenFacade
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  private final AuthUserProfileProviderImpl authUserProfileProvider;
  private final UserDetailsLoader userDetailsLoader;
  private final QueryGateway queryGateway;
  private final CommandGateway commandGateway;
  private final TokenFacade tokenUtils;

  /**
   * Constructor for the class.
   *
   * @param authUserProfileProvider The provider for user profiles during authentication
   * @param userDetailsLoader      Loader for user details
   * @param queryGateway           Axon Framework query gateway
   * @param commandGateway         Axon Framework command gateway
   * @param tokenUtils             Utility for token operations
   */
  @Autowired
  public SecurityConfig(AuthUserProfileProviderImpl authUserProfileProvider,
                        UserDetailsLoader userDetailsLoader,
                        QueryGateway queryGateway,
                        CommandGateway commandGateway,
                        TokenFacade tokenUtils) {
    this.authUserProfileProvider = authUserProfileProvider;
    this.userDetailsLoader = userDetailsLoader;
    this.queryGateway = queryGateway;
    this.commandGateway = commandGateway;
    this.tokenUtils = tokenUtils;
  }

  /**
   * Configures the security filter chain for authentication.
   *
   * @param http The HttpSecurity object to configure
   * @return The configured SecurityFilterChain
   * @throws Exception If an error occurs during configuration
   */
  @Bean
  protected SecurityFilterChain filterChainAuth(HttpSecurity http) throws Exception {
    http.securityMatcher(new AntPathRequestMatcher("/auth/**"));

    configureDefault(http);

    configureAuthFilters(http);

    http.addFilterAfter(tokenTypeFilter(), LoadUserFromDatabaseFilterByJwt.class);

    http.authenticationProvider(authUserProfileProvider);
    return http.build();
  }

  /**
   * Configures the security filter chain for general use.
   *
   * @param http The HttpSecurity object to configure
   * @return The configured SecurityFilterChain
   * @throws Exception If an error occurs during configuration
   */
  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.securityMatcher(new AntPathRequestMatcher("/login"));
    http.securityMatcher(new AntPathRequestMatcher("/registration"));

    configureDefault(http);
    return http.build();
  }

  /**
   * Customizes web security settings.
   *
   * @return The WebSecurityCustomizer for additional customization
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
      .requestMatchers(new AntPathRequestMatcher("/h2-console/**"))
      .requestMatchers(new AntPathRequestMatcher("/auth/login"));
  }

  /**
   * Configures authentication filters for the security filter chain.
   *
   * @param http The HttpSecurity object to configure
   * @throws Exception If an error occurs during configuration
   */
  private void configureAuthFilters(HttpSecurity http) throws Exception {
    http
      .addFilterBefore(new ErrorHandlingFilter(new ObjectMapper()), UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(new TokenRemoverFilter(), ErrorHandlingFilter.class)
      .addFilterAfter(new SignatureVerificationFilter(tokenUtils), TokenRemoverFilter.class)
      .addFilterAfter(new LoadUserFromDatabaseFilterByJwt(userDetailsLoader), SignatureVerificationFilter.class);
  }

  /**
   * Configures default security settings for the HttpSecurity object.
   *
   * @param http The HttpSecurity object to configure
   * @throws Exception If an error occurs during configuration
   */
  private void configureDefault(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
      .formLogin(formLogin -> formLogin.disable())
      .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  }

  /**
   * Creates a URI filter based on token types.
   *
   * @return The URI filter
   */
  private URIFilter tokenTypeFilter() {
    return new URIFilter(jwtRefreshFilter());
  }

  /**
   * Creates a JWT refresh filter.
   *
   * @return The JWT refresh filter
   */
  private JwtRefreshFilter jwtRefreshFilter() {
    return new JwtRefreshFilter(tokenGenerationFilter());
  }

  /**
   * Creates a token generation filter.
   *
   * @return The token generation filter
   */
  private TokenGenerationFilter tokenGenerationFilter() {
    return new TokenGenerationFilter(commandGateway, new ObjectMapper());
  }

}
