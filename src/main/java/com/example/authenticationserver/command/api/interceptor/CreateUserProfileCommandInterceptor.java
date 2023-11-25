/**
 * CreateUserProfileCommandInterceptor:
 * Intercepts the {@link CreateUserProfileCommand} before sending it for execution and checks if the user ID or username already exists.
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code QueryGateway}: The gateway for performing queries.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * The {@code CreateUserProfileCommandInterceptor} is responsible for intercepting the {@link CreateUserProfileCommand}
 * and querying the {@link UserProfileLookupEntity} to check if the user ID or username already exists before executing the command.
 * If a user profile with the same user ID or username is found, an exception is thrown.
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * CreateUserProfileCommandInterceptor commandInterceptor = new CreateUserProfileCommandInterceptor(queryGateway);
 * CreateUserProfileCommand createUserProfileCommand = // create a create user profile command
 * commandInterceptor.handle(Collections.singletonList(createUserProfileCommand));
 * // If a user profile with the same user ID or username already exists, an exception is thrown.
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> [Author Name]</p>
 *
 * @see com.example.authenticationserver.query.api.consistency.UserProfileLookupEntity
 * @see com.project.core.commands.user.CreateUserProfileCommand
 * @see org.axonframework.messaging.MessageDispatchInterceptor
 */
package com.example.authenticationserver.command.api.interceptor;

import com.example.authenticationserver.query.api.consistency.UserProfileLookupEntity;
import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.queries.user.UserProfileLookupQuery;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

@Component
public class CreateUserProfileCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
  private final QueryGateway queryGateway;

  @Autowired
  public CreateUserProfileCommandInterceptor(QueryGateway queryGateway) {
    this.queryGateway = queryGateway;
  }

  /**
   * Intercepts the {@link CreateUserProfileCommand} before sending it for execution.
   * Queries the user profile lookup repository to check if a user with the same user ID or username already exists.
   * If a user profile is found, an exception is thrown.
   *
   * @param list The list of messages (commands) to intercept.
   * @return The intercepted command message.
   */
  @Nonnull
  @Override
  public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
    @Nonnull List<? extends CommandMessage<?>> list) {
    return (index, command) -> {
      if (CreateUserProfileCommand.class.equals(command.getPayloadType())) {

        CreateUserProfileCommand createUserProfileCommand = (CreateUserProfileCommand) command.getPayload();

        var query = UserProfileLookupQuery.builder()
          .userId(createUserProfileCommand.getUserId())
          .userName(createUserProfileCommand.getUserName())
          .build();

        Optional<UserProfileLookupEntity> existingUser = queryGateway.query(query, Optional.class).join();
        if (existingUser.isPresent()) {
          throw new IllegalStateException(String.format("A user with the same ID \"%s\" or username \"%s\" already exists",
            existingUser.get().getUserId(), existingUser.get().getUserName()));
        }
      }
      return command;

    };
  }
}
