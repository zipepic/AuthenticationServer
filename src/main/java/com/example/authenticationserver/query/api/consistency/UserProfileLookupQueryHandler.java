
/**
 * UserProfileLookupQueryHandler:
 * Handles queries related to user profile lookup and retrieves the corresponding user profile from the repository.
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code UserProfileLookupRepository}: The repository for accessing user profile lookup entities.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * The {@code UserProfileLookupQueryHandler} is responsible for handling {@link UserProfileLookupQuery} queries
 * and retrieving the user profile lookup entity based on the query parameters. It is automatically registered as a query handler
 * by Axon Framework.
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * UserProfileLookupQueryHandler userProfileQueryHandler = new UserProfileLookupQueryHandler(userProfileRepository);
 * UserProfileLookupQuery userProfileLookupQuery = // create a user profile lookup query
 * Optional<UserProfileLookupEntity> userProfile = userProfileQueryHandler.handle(userProfileLookupQuery);
 * if (userProfile.isPresent()) {
 *   // User profile found based on the query parameters.
 * } else {
 *   // No user profile found.
 * }
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> Konstantin Kokoshnikov</p>
 *
 * @see com.example.authenticationserver.query.api.consistency.UserProfileLookupRepository
 * @see com.project.core.queries.user.UserProfileLookupQuery
 */

package com.example.authenticationserver.query.api.consistency;

import com.project.core.queries.user.UserProfileLookupQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class UserProfileLookupQueryHandler {
  private final UserProfileLookupRepository userProfileRepository;

  @Autowired
  public UserProfileLookupQueryHandler(UserProfileLookupRepository userProfileRepository) {
    this.userProfileRepository = userProfileRepository;
  }

  /**
   * Handles the {@link UserProfileLookupQuery} query.
   * Retrieves the user profile lookup entity from the repository based on the query parameters.
   *
   * @param query The user profile lookup query.
   * @return The optional user profile lookup entity, if found.
   */
  @QueryHandler
  public Optional<UserProfileLookupEntity> handle(UserProfileLookupQuery query){
    return userProfileRepository.findByUserIdOrUserName(query.getUserId(), query.getUserName());
  }
}
