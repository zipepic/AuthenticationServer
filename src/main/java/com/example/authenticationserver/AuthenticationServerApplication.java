package com.example.authenticationserver;

import com.example.authenticationserver.saga.UserCreationSaga;
import com.project.core.commands.token.CancelTokenCreationCommand;
import com.project.core.commands.token.CreateTokenCommand;
import com.project.core.commands.token.GenerateJwtTokenCommand;
import com.project.core.commands.token.RefreshJwtTokenCommand;
import com.project.core.dto.JwksDTO;
import com.project.core.dto.TokenSummary;
import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.project.core.dto.TokenAuthorizationCodeDTO;
import com.nimbusds.jose.jwk.JWK;
import com.project.core.commands.user.*;
import com.project.core.dto.UserProfileDTO;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import com.project.core.events.token.TokenGeneratedEvent;
import com.project.core.events.user.*;
import com.project.core.queries.FetchJwksQuery;
import com.project.core.queries.user.*;
import com.thoughtworks.xstream.XStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tokenlib.util.jwk.AuthProvider;
import tokenlib.util.jwk.SimpleJWK;

@SpringBootApplication
public class AuthenticationServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuthenticationServerApplication.class, args);
  }

  @Bean
  public XStream xStream() {
    XStream xStream = new XStream();
    // Register classes for events
    registerClasses(xStream,
      // Event classes from com.project.core.events.user package
      UserProfileCreatedEvent.class,
      UserProfileUpdatedEvent.class,
      UserProfilePasswordChangedEvent.class);

    // Register classes for queries
    registerClasses(xStream,
      // Query classes from com.project.core.queries.user package
      FetchUserProfileByUserIdQuery.class,
      FetchUserProfileByUserNameQuery.class,
      UserProfileLookupQuery.class);

    // Register classes for commands
    registerClasses(xStream,
      // Command classes from com.project.core.commands.user package
      CreateUserProfileCommand.class,
      UpdateUserProfileCommand.class,
      ChangeUserProfilePasswordCommand.class);
    registerClasses(xStream,
      // Command classes from com.project.core.commands.user package
      BindProviderIdToUserCommand.class,
      CreateUserFromProviderIdCommand.class,
            ProviderIdBoundToUserEvent.class,
            UserCreatedFromProviderIdEvent.class,
            CheckUserProfileByProviderIdQuery.class,
            UserProfileProviderMappingLookUpCreatedEvent.class,
            CancelTokenCreationCommand.class,
            CreateTokenCommand.class,
            CancelUserCreationCommand.class,
            CompleteWereUserCreationCommand.class,
            TokenCanceledEvent.class,
            TokenCreatedEvent.class,
            UserCanceledCreationEvent.class,
            UserWereCompletedEvent.class,
            GenerateJwtTokenCommand.class,
            TokenGeneratedEvent.class,
            FetchJwksQuery.class,
            JwksDTO.class,
            RefreshJwtTokenCommand.class,
            FetchUserProfileDTOByUserIdQuery.class,
            FindUserIdByUserNameAndValidatePasswordQuery.class);

    // Register other necessary classes
    registerClasses(xStream,
      TokenSummary.class,
      UserProfileEntity.class,
      TokenAuthorizationCodeDTO.class,
      JWK.class,
      SimpleJWK.class,
            AuthProvider.class,
            UserCreationSaga.class,
            UserProfileDTO.class);

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
