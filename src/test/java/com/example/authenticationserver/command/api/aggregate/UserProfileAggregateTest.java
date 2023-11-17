package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.events.user.UserProfileCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class UserProfileAggregateTest {
  private FixtureConfiguration<UserProfileAggregate> fixture;
  private Instant fixedTime;

  @BeforeEach
  public void setUp() {
    fixture = new AggregateTestFixture<>(UserProfileAggregate.class);
    fixedTime = Instant.parse("2023-01-01T00:00:00Z");

    // Fix: Use Instant instead of Date for consistent time handling
    fixture.givenCurrentTime(fixedTime);
  }

  @Test
  public void aggregateValidationWhenExpect() {
    var command = CreateUserProfileCommand.builder()
      .userId(UUID.randomUUID().toString())
      .userName("test")
      .passwordHash("test")
      .build();

    // Fix: Use fixedTime for createdAt to ensure consistent testing
    var expectedEvent = UserProfileCreatedEvent.builder()
      .userId(command.getUserId())
      .userName(command.getUserName())
      .passwordHash(command.getPasswordHash())
      .userStatus("CREATED")
      .role("ROLE_USER")
      .createdAt(Date.from(fixedTime))
      .build();

    fixture.givenNoPriorActivity()
      .when(command)
      .expectSuccessfulHandlerExecution()
      .expectEvents(expectedEvent);
  }

  @Test
  public void aggregateValidationState() {
    var command = CreateUserProfileCommand.builder()
      .userId(UUID.randomUUID().toString())
      .userName("test")
      .passwordHash("test")
      .build();

    fixture.givenNoPriorActivity()
      .when(command)
      .expectState(state -> {
        state.getUserId().equals(command.getUserId());
        state.getUserName().equals(command.getUserName());
        state.getPasswordHash().equals(command.getPasswordHash());
        state.getUserStatus().equals("CREATED");
        state.getRole().equals("ROLE_USER");
        // Fix: Use fixedTime for createdAt to ensure consistent testing
        state.getCreatedAt().equals(Date.from(fixedTime));
      });
  }
}
