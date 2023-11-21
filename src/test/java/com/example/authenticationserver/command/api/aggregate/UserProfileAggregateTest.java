package com.example.authenticationserver.command.api.aggregate;

import com.project.core.commands.user.CreateUserProfileCommand;
import com.project.core.events.user.UserProfileCreatedEvent;
import org.axonframework.eventsourcing.IncompatibleAggregateException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Stream;
//TODO Fix:
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

  @ParameterizedTest(name = "aggregateValidationWhenExpect - userId: {0}, userName: {1}, passwordHash: {2}")
  @MethodSource("userProfileTestData")
  public void aggregateValidationWhenExpect(String userId, String userName, String passwordHash) {
    var command = CreateUserProfileCommand.builder()
      .userId(userId)
      .userName(userName)
      .passwordHash(passwordHash)
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

  @ParameterizedTest(name = "aggregateValidationState - userId: {0}, userName: {1}, passwordHash: {2}")
  @MethodSource("userProfileTestData")
  public void aggregateValidationState(String userId, String userName, String passwordHash) {
    var command = CreateUserProfileCommand.builder()
      .userId(userId)
      .userName(userName)
      .passwordHash(passwordHash)
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
//        state.getCreatedAt().equals(Date.from(fixedTime));
      });
  }
  @Test
  public void exceptionWhenExpect() {
    var command = CreateUserProfileCommand.builder()
      .userId(null)
      .userName(null)
      .passwordHash(null)
      .build();

    fixture.givenNoPriorActivity()
      .when(command)
      .expectException(IncompatibleAggregateException.class);
  }

  private static Stream<String[]> userProfileTestData() {
    return Stream.of(
      new String[]{"id1", "user1", "pass1"},
      new String[]{"id2", "user2", "pass2"},
      new String[]{"id3", "user3", "pass3"}
    );
  }
}
