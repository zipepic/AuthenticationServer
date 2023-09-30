package com.example.authenticationserver;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.project.core.commands.*;
import com.project.core.events.*;
import com.project.core.queries.CheckLoginDataQuery;
import com.project.core.queries.FindUserIdByOneTimeCodeQuery;
import com.project.core.queries.FindUserIdByUserNameQuery;
import com.thoughtworks.xstream.XStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthenticationServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationServerApplication.class, args);
  }

  @Bean
  public XStream xStream() {
    XStream xStream = new XStream();
    xStream.allowTypeHierarchy(CreateUserProfileCommand.class);
    xStream.allowTypeHierarchy(UserProfileCreatedEvent.class);
    xStream.allowTypeHierarchy(CreateApplicationCommand.class);
    xStream.allowTypeHierarchy(RegisterApplicationCommand.class);
    xStream.allowTypeHierarchy(ApplicationCreatedEvent.class);
    xStream.allowTypeHierarchy(LoginApplicationCommand.class);
    xStream.allowTypeHierarchy(ApplicationLoggedInEvent.class);
    xStream.allowTypeHierarchy(ApplicationRegisteredEvent.class);
    xStream.allowTypeHierarchy(TokenSummary.class);
    xStream.allowTypeHierarchy(FindUserIdByUserNameQuery.class);
    xStream.allowTypeHierarchy(GenerateOneTimeCodeUserProfileCommand.class);
    xStream.allowTypeHierarchy(OneTimeCodeUserProfileGeneratedEvent.class);
    xStream.allowTypeHierarchy(UseOneTimeCodeCommand.class);
    xStream.allowTypeHierarchy(CheckLoginDataQuery.class);
    xStream.allowTypeHierarchy(FindUserIdByOneTimeCodeQuery.class);
    return xStream;
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
