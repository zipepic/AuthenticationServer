package com.example.authenticationserver;

import com.project.core.commands.CreateApplicationCommand;
import com.project.core.commands.CreateUserProfileCommand;
import com.project.core.commands.LoginApplicationCommand;
import com.project.core.commands.RegisterApplicationCommand;
import com.project.core.events.ApplicationCreatedEvent;
import com.project.core.events.ApplicationLoggedInEvent;
import com.project.core.events.UserProfileCreatedEvent;
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
    return xStream;
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
