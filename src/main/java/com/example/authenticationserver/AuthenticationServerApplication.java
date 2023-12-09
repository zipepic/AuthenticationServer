package com.example.authenticationserver;

import com.project.core.commands.token.CancelTokenCreationCommand;
import com.project.core.commands.token.CreateTokenCommand;
import com.project.core.dto.TokenSummary;
import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.project.core.dto.TokenAuthorizationCodeDTO;
import com.nimbusds.jose.jwk.JWK;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.commands.app.CreateApplicationCommand;
import com.project.core.commands.user.*;
import com.project.core.events.code.AuthorizationCodeGeneratedEvent;
import com.project.core.events.code.AuthorizationCodeUsedEvent;
import com.project.core.events.token.TokenCanceledEvent;
import com.project.core.events.token.TokenCreatedEvent;
import com.project.core.events.user.*;
import com.project.core.events.app.ApplicationCreatedEvent;
import com.project.core.queries.app.CheckLoginDataQuery;
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
      // Event classes from com.project.core.events.code package
      AuthorizationCodeGeneratedEvent.class,
      AuthorizationCodeUsedEvent.class,
      // Event classes from com.project.core.events.user package
      UserProfileCreatedEvent.class,
      UserProfileUpdatedEvent.class,
      UserProfilePasswordChangedEvent.class,
      JwtTokenInfoEvent.class,
      JwkTokenInfoEvent.class,
      // Event classes from com.project.core.events.app package
      ApplicationCreatedEvent.class);

    // Register classes for queries
    registerClasses(xStream,
      // Query classes from com.project.core.queries.app package
      CheckLoginDataQuery.class,
      // Query classes from com.project.core.queries.user package
      FetchUserProfileByUserIdQuery.class,
      FetchUserProfileByUserNameQuery.class,
      ValidateRefreshTokenForUserProfileQuery.class,
      FetchJwkSet.class,
      UserProfileLookupQuery.class);

    // Register classes for commands
    registerClasses(xStream,
      // Command classes from com.project.core.commands.code package
      GenerateAuthorizationCodeCommand.class,
      UseAuthorizationCodeCommand.class,
      // Command classes from com.project.core.commands.user package
      CreateUserProfileCommand.class,
      UpdateUserProfileCommand.class,
      ChangeUserProfilePasswordCommand.class,
      GenerateRefreshTokenForUserProfileCommand.class,
      RefreshAccessTokenForUserProfileCommand.class,
      // Command classes from com.project.core.commands.app package
      CreateApplicationCommand.class);
    registerClasses(xStream,
      // Command classes from com.project.core.commands.user package
      BindProviderIdToUserCommand.class,
      CreateUserFromProviderIdCommand.class,
            ProviderIdBoundToUserEvent.class,
            UserCreatedFromProviderIdEvent.class,
            CheckUserProfileByProviderIdQuery.class,
            GenerateTokenByProviderIdCommand.class,
            UserProfileProviderMappingLookUpCreatedEvent.class,
            CancelTokenCreationCommand.class,
            CreateTokenCommand.class,
            CancelUserCreationCommand.class,
            CompleteWereUserCreationCommand.class,
            TokenCanceledEvent.class,
            TokenCreatedEvent.class,
            UserCanceledCreationEvent.class,
            UserWereCompletedEvent.class);

    // Register other necessary classes
    registerClasses(xStream,
      TokenSummary.class,
      UserProfileEntity.class,
      TokenAuthorizationCodeDTO.class,
      JWK.class,
      SimpleJWK.class,
            AuthProvider.class);

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
