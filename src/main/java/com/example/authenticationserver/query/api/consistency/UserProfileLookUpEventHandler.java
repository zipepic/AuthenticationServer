/**
 * UserProfileLookUpEventHandler:
 * Handles the events related to user profile creation and updates the user profile lookup repository accordingly.
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code UserProfileLookupRepository}: The repository for accessing and persisting user profile lookup entities.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * The {@code UserProfileLookUpEventHandler} is responsible for handling the {@link UserProfileCreatedEvent} event
 * and updating the user profile lookup repository based on the event data. It is automatically registered as an event handler
 * by Axon Framework.
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * UserProfileLookUpEventHandler userProfileHandler = new UserProfileLookUpEventHandler(userProfileRepository);
 * UserProfileCreatedEvent userProfileCreatedEvent = // create a user profile created event
 * userProfileHandler.on(userProfileCreatedEvent);
 * // User profile entity is created in the user profile lookup repository.
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> Konstantin Kokoshnikov</p>
 *
 * @see com.example.authenticationserver.query.api.consistency.UserProfileLookupRepository
 * @see com.project.core.events.user.UserProfileCreatedEvent
 */

package com.example.authenticationserver.query.api.consistency;

import com.project.core.events.user.UserProfileCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("user-profile-group")
public final class UserProfileLookUpEventHandler {
  private final UserProfileLookupRepository userProfileRepository;

  @Autowired
  public UserProfileLookUpEventHandler(UserProfileLookupRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  /**
   * Handles the {@link UserProfileCreatedEvent} event.
   * Copies the event data to a new {@link UserProfileLookupEntity} and saves it to the user profile lookup repository.
   *
   * @param event The user profile created event.
   */
  @EventHandler
  public void on(UserProfileCreatedEvent event){
    UserProfileLookupEntity userProfileEntity = new UserProfileLookupEntity();
    BeanUtils.copyProperties(event, userProfileEntity);
    userProfileRepository.save(userProfileEntity);
  }
}
