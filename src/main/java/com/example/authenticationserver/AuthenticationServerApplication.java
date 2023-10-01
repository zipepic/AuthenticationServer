package com.example.authenticationserver;

import com.example.authenticationserver.command.api.restmodel.TokenSummary;
import com.project.core.commands.GenerateAuthorizationCodeCommand;
import com.project.core.commands.UseAuthorizationCodeCommand;
import com.project.core.commands.app.CreateApplicationCommand;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateOneTimeCodeUserProfileCommand;
import com.project.core.commands.user.UseOneTimeCodeCommand;
import com.project.core.events.AuthorizationCodeGeneratedEvent;
import com.project.core.events.AuthorizationCodeUsedEvent;
import com.project.core.events.user.OneTimeCodeUserProfileGeneratedEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import com.project.core.events.app.ApplicationCreatedEvent;
import com.project.core.queries.app.CheckLoginDataQuery;
import com.project.core.queries.user.FindUserIdByOneTimeCodeQuery;
import com.project.core.queries.user.FindUserIdByUserNameQuery;
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
    registerClasses(xStream,
      CreateUserProfileCommand.class,
      UserProfileCreatedEvent.class,
      CreateApplicationCommand.class,
      ApplicationCreatedEvent.class,
      TokenSummary.class,
      FindUserIdByUserNameQuery.class,
      GenerateOneTimeCodeUserProfileCommand.class,
      OneTimeCodeUserProfileGeneratedEvent.class,
      UseOneTimeCodeCommand.class,
      CheckLoginDataQuery.class,
      FindUserIdByOneTimeCodeQuery.class,
      GenerateAuthorizationCodeCommand.class,
      UseAuthorizationCodeCommand.class,
      AuthorizationCodeGeneratedEvent.class,
      AuthorizationCodeUsedEvent.class);
    return xStream;
  }

  private void registerClasses(XStream xStream, Class<?>... classes) {
    for (Class<?> clazz : classes) {
      xStream.allowTypeHierarchy(clazz);
    }
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
