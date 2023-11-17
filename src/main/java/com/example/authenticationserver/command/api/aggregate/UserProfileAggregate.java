package com.example.authenticationserver.command.api.aggregate;

/**
 * The {@code UserProfileAggregate} class represents the aggregate responsible for managing user profiles
 * within the authentication server. It utilizes the Axon Framework to handle commands, events, and event sourcing.
 *
 * <p><strong>Properties:</strong></p>
 * <ul>
 *   <li>{@code userId}: Unique identifier for the user profile.</li>
 *   <li>{@code userName}: User's username.</li>
 *   <li>{@code passwordHash}: Hashed password for security.</li>
 *   <li>{@code userStatus}: Current status of the user profile.</li>
 *   <li>{@code role}: Role assigned to the user.</li>
 *   <li>{@code tokenId}: Identifier for the user's authentication token.</li>
 *   <li>{@code createdAt}: Date when the user profile was created.</li>
 * </ul>
 *
 * <p><strong>Constructors:</strong></p>
 * <ul>
 *   <li>{@code UserProfileAggregate()}: Default constructor for Axon Framework.</li>
 *   <li>{@code UserProfileAggregate(CreateUserProfileCommand command)}:
 *       Constructor for creating a new user profile based on the {@code CreateUserProfileCommand}.</li>
 * </ul>
 *
 * <p><strong>Event Handlers:</strong></p>
 * <ul>
 *   <li>{@code on(UserProfileCreatedEvent event)}: Event handler for the {@code UserProfileCreatedEvent}.
 *   Updates the aggregate's properties based on the information provided in the event.</li>
 *
 *   <li>{@code on(JwtTokenInfoEvent event)}: Event handler for the {@code JwtTokenInfoEvent}.
 *   Updates the {@code tokenId} property based on the information provided in the event.</li>
 * </ul>
 *
 * <p><strong>Command Handlers:</strong></p>
 * <ol>
 *   <li>{@code handleGenerateTokenCommand(GenerateRefreshTokenForUserProfileCommand command,
 *   @NonNull UserProfileService service)}:
 *   Handles the generation of a new authentication token based on the
 *   {@code GenerateRefreshTokenForUserProfileCommand}. Invokes the {@code UserProfileService} to generate tokens
 *   and applies the relevant token information event.</li>
 *
 *   <li>{@code handleRefreshTokenCommand(RefreshAccessTokenForUserProfileCommand command,
 *   @NonNull UserProfileService service)}:
 *   Handles the refreshment of the authentication token based on the
 *   {@code RefreshAccessTokenForUserProfileCommand}. Invokes the {@code UserProfileService} to refresh tokens
 *   and applies the relevant token information event.</li>
 * </ol>
 *
 * <p><strong>Methods:</strong></p>
 * <ul>
 *   <li>{@code UserProfileAggregate()}: Default constructor for Axon Framework.</li>
 *   <li>{@code handleGenerateTokenCommand(GenerateRefreshTokenForUserProfileCommand command,
 *   @NonNull UserProfileService service)}:
 *   Handles the generation of a new authentication token based on the
 *   {@code GenerateRefreshTokenForUserProfileCommand}. Invokes the {@code UserProfileService} to generate tokens
 *   and applies the relevant token information event.</li>
 *
 *   <li>{@code handleRefreshTokenCommand(RefreshAccessTokenForUserProfileCommand command,
 *   @NonNull UserProfileService service)}:
 *   Handles the refreshment of the authentication token based on the
 *   {@code RefreshAccessTokenForUserProfileCommand}. Invokes the {@code UserProfileService} to refresh tokens
 *   and applies the relevant token information event.</li>
 * </ul>
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code UserProfileService}:
 *       A service responsible for handling user profile-related operations,
 *       such as token generation and refresh.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <p>The {@code UserProfileAggregate} is used to manage user profiles in response to commands and events
 * within the authentication server. It integrates with the {@code UserProfileService} for token-related functionality.</p>
 *
 * <p><strong>Important Notes:</strong></p>
 * <ul>
 *   <li>This class relies on the Axon Framework for event sourcing and command handling.</li>
 *   <li>Token-related operations are delegated to the {@code UserProfileService}.</li>
 * </ul>
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * UserProfileAggregate userProfile = new UserProfileAggregate();
 * CreateUserProfileCommand createUserProfileCommand = // create a command
 * userProfile.handle(createUserProfileCommand);
 * // User profile created, events applied, and aggregate updated accordingly.
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> XZZ1P</p>
 *
 * <p><strong>See Also:</strong></p>
 * <ul>
 *   <li>{@link com.example.authenticationserver.command.api.service.UserProfileService}</li>
 *   <li>{@link com.project.core.commands.user.CreateUserProfileCommand}</li>
 *   <li>{@link com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand}</li>
 *   <li>{@link com.project.core.commands.user.RefreshAccessTokenForUserProfileCommand}</li>
 *   <li>{@link com.project.core.events.user.JwkTokenInfoEvent}</li>
 *   <li>{@link com.project.core.events.user.JwtTokenInfoEvent}</li>
 *   <li>{@link com.project.core.events.user.UserProfileCreatedEvent}</li>
 * </ul>
 */

import com.example.authenticationserver.command.api.service.UserProfileService;
import com.example.authenticationserver.dto.TokenDTO;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand;
import com.project.core.commands.user.RefreshAccessTokenForUserProfileCommand;
import com.project.core.events.user.JwtTokenInfoEvent;
import com.project.core.events.user.UserProfileCreatedEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Aggregate
public class UserProfileAggregate {

  @AggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private String role;
  private String tokenId;
  private Date createdAt;

  /**
   * Default constructor for Axon Framework.
   */
  public UserProfileAggregate() {}

  /**
   * Constructor for creating a new user profile based on the {@code CreateUserProfileCommand}.
   *
   * @param command The command containing information to create a new user profile.
   */
  @CommandHandler
  public UserProfileAggregate(CreateUserProfileCommand command) {
    var event = UserProfileCreatedEvent.builder()
      .userId(command.getUserId())
      .userName(command.getUserName())
      .passwordHash(command.getPasswordHash())
      .userStatus("CREATED")
      .role("ROLE_USER")
      .createdAt(new Date())
      .build();
    AggregateLifecycle.apply(event);
  }

  /**
   * Event handler for the {@code UserProfileCreatedEvent}. Updates the aggregate's properties
   * based on the information provided in the event.
   *
   * @param event The event containing information about the created user profile.
   */
  @EventSourcingHandler
  public void on(UserProfileCreatedEvent event) {
    this.userId = event.getUserId();
    this.userName = event.getUserName();
    this.passwordHash = event.getPasswordHash();
    this.userStatus = event.getUserStatus();
    this.role = event.getRole();
    this.createdAt = event.getCreatedAt();
  }

  /**
   * Handles the generation of a new authentication token based on the
   * {@code GenerateRefreshTokenForUserProfileCommand}. Invokes the {@code UserProfileService} to generate tokens
   * and applies the relevant token information event.
   *
   * @param command The command containing information to generate a new authentication token.
   * @param service The {@code UserProfileService} for token-related operations.
   * @return The {@code TokenDTO} representing the generated tokens.
   */
  @CommandHandler
  public TokenDTO handleGenerateTokenCommand(GenerateRefreshTokenForUserProfileCommand command, @NonNull UserProfileService service) {
    UUID tokenId = UUID.randomUUID();
    var map = service.generateJwtTokensMap(command.getUserId(), tokenId.toString());
    AggregateLifecycle.apply(service.handleTokenInfoEvent(
      command.getUserId(),
      map,
      tokenId.toString()
    ));
    return service.makeTokenDTO(map, tokenId.toString());
  }

  /**
   * Handles the refreshment of the authentication token based on the
   * {@code RefreshAccessTokenForUserProfileCommand}. Invokes the {@code UserProfileService} to refresh tokens
   * and applies the relevant token information event.
   *
   * @param command The command containing information to refresh the authentication token.
   * @param service The {@code UserProfileService} for token-related operations.
   * @return The {@code TokenDTO} representing the refreshed tokens.
   */
  @CommandHandler
  public TokenDTO handleRefreshTokenCommand(RefreshAccessTokenForUserProfileCommand command, @NonNull UserProfileService service) {
    UUID tokenId = UUID.randomUUID();
    var map = service.refreshToken(command.getRefreshToken(), tokenId.toString());
    AggregateLifecycle.apply(service.handleTokenInfoEvent(
      command.getUserId(),
      map,
      tokenId.toString()
    ));
    return service.makeTokenDTO(map, tokenId.toString());
  }

  /**
   * Event handler for the {@code JwtTokenInfoEvent}. Updates the {@code tokenId} property
   * based on the information provided in the event.
   *
   * @param event The event containing information about the JWT token.
   */
  @EventSourcingHandler
  public void on(JwtTokenInfoEvent event) {
    this.tokenId = event.getTokenId();
  }
}
