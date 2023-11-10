package com.example.authenticationserver;

import com.example.authenticationserver.dto.TokenSummary;
import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.util.newutil.TokenFacade;
import com.example.authenticationserver.util.newutil.TokenProcessorFactory;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.commands.app.CreateApplicationCommand;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.commands.user.RefreshAccessTokenForUserProfileCommand;
import com.project.core.events.code.AuthorizationCodeGeneratedEvent;
import com.project.core.events.code.AuthorizationCodeUsedEvent;
import com.project.core.events.user.RefreshTokenForUserProfileGeneratedEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import com.project.core.events.app.ApplicationCreatedEvent;
import com.project.core.queries.app.CheckLoginDataQuery;
import com.project.core.queries.user.FetchUserProfileByUserIdQuery;
import com.project.core.queries.user.FetchUserProfileByUserNameQuery;
import com.project.core.queries.user.FindUserIdByUserNameQuery;
import com.project.core.queries.user.ValidateRefreshTokenForUserProfileQuery;
import com.thoughtworks.xstream.XStream;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

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
      CheckLoginDataQuery.class,
      GenerateAuthorizationCodeCommand.class,
      UseAuthorizationCodeCommand.class,
      AuthorizationCodeGeneratedEvent.class,
      AuthorizationCodeUsedEvent.class,
      UserProfileEntity.class,
      FetchUserProfileByUserIdQuery.class,
      FetchUserProfileByUserNameQuery.class,
      TokenAuthorizationCodeDTO.class,
      GenerateRefreshTokenForUserProfileCommand.class,
      RefreshTokenForUserProfileGeneratedEvent.class,
      RefreshAccessTokenForUserProfileCommand.class,
      ValidateRefreshTokenForUserProfileQuery.class);
    return xStream;
  }

  private void registerClasses(XStream xStream, Class<?>... classes) {
    for (Class<?> clazz : classes) {
      xStream.allowTypeHierarchy(clazz);
    }
  }
  @Bean
  public SecretKeySpec secretKeySpec(@NonNull @Value("${app.secret:#{null}}") String secret) {
    byte[] secretKeyBytes = Base64.getDecoder().decode(secret);
    return new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
