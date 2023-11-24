/**
 * UserProfileLookupRepository:
 * Provides CRUD operations for accessing and persisting user profile lookup entities in the database.
 *
 * <p><strong>Usage:</strong></p>
 * The {@code UserProfileLookupRepository} interface extends the {@link org.springframework.data.jpa.repository.JpaRepository}
 * interface, which provides basic CRUD operations for the {@link UserProfileLookupEntity} entities.
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * UserProfileLookupRepository userProfileRepository = new UserProfileLookupRepositoryImpl();
 * Optional<UserProfileLookupEntity> userProfile = userProfileRepository.findByUserIdOrUserName(userId, userName);
 * if (userProfile.isPresent()) {
 *   // User profile found based on the user ID or username.
 * } else {
 *   // No user profile found.
 * }
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> [Author Name]</p>
 *
 * @see com.example.authenticationserver.query.api.consistency.UserProfileLookupEntity
 */

package com.example.authenticationserver.query.api.consistency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileLookupRepository extends JpaRepository<UserProfileLookupEntity, String> {

  /**
   * Finds a user profile lookup entity based on the user ID or username.
   *
   * @param userId The user ID.
   * @param userName The username.
   * @return The optional user profile lookup entity, if found.
   */
  Optional<UserProfileLookupEntity> findByUserIdOrUserName(String userId, String userName);
}

