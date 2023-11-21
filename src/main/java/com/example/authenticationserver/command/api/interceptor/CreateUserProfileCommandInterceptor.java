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

  @Nonnull
  @Override
  public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
    @Nonnull List<? extends CommandMessage<?>> list) {
    return (index, command) -> {
      if(CreateUserProfileCommand.class.equals(command.getPayloadType())){

        CreateUserProfileCommand createUserProfileCommand
          = (CreateUserProfileCommand) command.getPayload();

        var query = UserProfileLookupQuery.builder()
          .userId(createUserProfileCommand.getUserId())
          .userName(createUserProfileCommand.getUserName())
          .build();

        Optional<UserProfileLookupEntity> user = queryGateway.query(query, Optional.class).join();
        if(user.isPresent()){
          throw new IllegalStateException(String.format("this user id %s or name %s is present",
            user.get().getUserId(), user.get().getUserName()));
        }
      }
      return command;
    };
  }
}
