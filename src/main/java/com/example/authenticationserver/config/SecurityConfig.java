package com.example.authenticationserver.config;

import com.example.authenticationserver.service.UserProfileDetailsService;
import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
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
public class SecurityConfig {

  private final AuthUserProfileProviderImpl authUserProfileProvider;
  private final UserProfileDetailsService userProfileDetailsService;
  @Autowired
  public SecurityConfig(AuthUserProfileProviderImpl authUserProfileProvider, UserProfileDetailsService userProfileDetailsService) {
    this.authUserProfileProvider = authUserProfileProvider;
    this.userProfileDetailsService = userProfileDetailsService;
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.formLogin(formLogin -> formLogin.disable());
    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authorizeHttpRequests(authorize ->
        authorize
          .requestMatchers(new AntPathRequestMatcher("/security/**")).hasRole("USER")
          .anyRequest().permitAll())
      .httpBasic(Customizer.withDefaults());
    http.addFilterBefore(new JwtFilter(userProfileDetailsService), UsernamePasswordAuthenticationFilter.class);
    http.authenticationProvider(authUserProfileProvider);
    return http.build();
  }
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().
      requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
  }
}

