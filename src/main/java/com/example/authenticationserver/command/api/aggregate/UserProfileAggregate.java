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
 * <p><strong>Author:</strong> Konstantin Kokoshnikov</p>
 *
 * <p><strong>See Also:</strong></p>
 * <ul>
 *   <li>{@link com.example.authenticationserver.command.api.service.UserProfileAggregateService}</li>
 *   <li>{@link com.project.core.commands.user.CreateUserProfileCommand}</li>
 *   <li>{@link com.project.core.commands.user.GenerateRefreshTokenForUserProfileCommand}</li>
 *   <li>{@link com.project.core.commands.user.RefreshAccessTokenForUserProfileCommand}</li>
 *   <li>{@link com.project.core.events.user.JwkTokenInfoEvent}</li>
 *   <li>{@link com.project.core.events.user.JwtTokenInfoEvent}</li>
 *   <li>{@link com.project.core.events.user.UserProfileCreatedEvent}</li>
 * </ul>
 */

import com.project.core.commands.user.*;
import com.project.core.events.user.*;
import lombok.Data;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import tokenlib.util.jwk.AuthProvider;

import java.util.Date;

@Aggregate
@Data
public class UserProfileAggregate {

  @AggregateIdentifier
  private String userId;
  private String userName;
  private String passwordHash;
  private String userStatus;
  private String role;
  private String githubId;
  private String googleId;
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
      .role("ROLE_USER_MAIN")
      .createdAt(new Date())
      .build();

    AggregateLifecycle.apply(event);
  }

  @CommandHandler
  public UserProfileAggregate(CreateUserFromProviderIdCommand command){
    var event = UserCreatedFromProviderIdEvent.builder()
            .userId(command.getUserId())
            .userName(command.getUserName())
            .providerId(command.getProviderId())
            .authProvider(command.getAuthProvider())
            .userStatus("CREATED")
            .role("ROLE_USER_TEST")
            .createdAt(new Date())
            .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(UserCreatedFromProviderIdEvent event) {
    this.userId = event.getUserId();
    this.userName = event.getUserName();
    if(event.getAuthProvider() == AuthProvider.GITHUB){
      this.setGithubId(event.getProviderId());
    }else if(event.getAuthProvider() == AuthProvider.GOOGLE){
      this.setGoogleId(event.getProviderId());
    }
  }
  @CommandHandler
  public void handle(CancelUserCreationCommand command){
    var event = UserCanceledCreationEvent.builder()
      .userId(command.getUserId())
      .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
    public void on(UserCanceledCreationEvent event) {
        this.userStatus = "CANCELLED";
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
  @CommandHandler
  public void handle(UpdateUserProfileCommand command){
    var event = UserProfileUpdatedEvent.builder()
      .userId(command.getUserId())
      .userName(command.getUserName())
      .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(UserProfileUpdatedEvent event) {
    this.userName = event.getUserName();
  }
  @CommandHandler
  public void handle(ChangeUserProfilePasswordCommand command){
    var event = UserProfilePasswordChangedEvent.builder()
      .userId(command.getUserId())
      .newPassword(command.getNewPassword())
      .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(UserProfilePasswordChangedEvent event) {
    this.passwordHash = event.getNewPassword();
  }
  @CommandHandler
  public void handle(BindProviderIdToUserCommand command){
    var event = ProviderIdBoundToUserEvent.builder()
      .userId(command.getUserId())
      .providerId(command.getProviderId())
      .providerType(command.getProviderType())
      .build();
    AggregateLifecycle.apply(event);
  }
  @EventSourcingHandler
  public void on(ProviderIdBoundToUserEvent event) {
    if(event.getProviderType().equals("github")){
      this.setGithubId(event.getProviderId());
    }else if(event.getProviderType().equals("google")){
      this.setGoogleId(event.getProviderId());
    }
  }
  @CommandHandler
  public void handle(CompleteWereUserCreationCommand command){
    var event = UserWereCompletedEvent.builder()
      .userId(command.getUserId())
            .providerId(this.githubId)
            .authProvider(AuthProvider.GITHUB)
            .status(command.getStatus())
      .build();
    AggregateLifecycle.apply(event);
  }
    @EventSourcingHandler
    public void on(UserWereCompletedEvent event) {
        this.userStatus = event.getStatus();
    }
}
