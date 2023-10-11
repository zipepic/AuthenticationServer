//package com.example.authenticationserver.config.filter;
//
//import com.example.authenticationserver.config.filter.auth.AuthenticationFilter;
//import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig {
//  private final CommandGateway commandGateway;
//  private final AuthUserProfileProviderImpl authUserProfileProvider;
//  @Autowired
//  public FilterConfig(CommandGateway commandGateway, AuthUserProfileProviderImpl authUserProfileProvider) {
//    this.commandGateway = commandGateway;
//    this.authUserProfileProvider = authUserProfileProvider;
//  }
//
//  @Bean
//  public AuthenticationFilter yourCustomFilter() {
//    return new AuthenticationFilter(authUserProfileProvider,commandGateway,new ObjectMapper());
//  }
//
//  @Bean
//  public FilterRegistrationBean<AuthenticationFilter> customFilter() {
//    FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//    registrationBean.setFilter(yourCustomFilter());
//    registrationBean.addUrlPatterns("/test/login");
//    return registrationBean;
//  }
//}
//
