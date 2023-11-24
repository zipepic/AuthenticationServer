/**
 * UserProfileLookupEntity:
 * Represents a user profile lookup entity in the authentication server.
 * This entity is used to store and retrieve user profile information.
 *
 * <p><strong>Attributes:</strong></p>
 * <ul>
 *   <li>{@code userId} (String): The unique identifier of the user profile.</li>
 *   <li>{@code userName} (String): The username of the user profile.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * The {@code UserProfileLookupEntity} class is used to map user profile data from the database to Java objects
 * and vice versa. It is typically utilized in the data access layer of the authentication server.
 *
 * <p><strong>Example:</strong></p>
 * <pre>
 * {@code
 * UserProfileLookupEntity userProfile = new UserProfileLookupEntity();
 * userProfile.setUserId("123456");
 * userProfile.setUserName("john_doe");
 * // Save the user profile entity to the database
 * userProfileRepository.save(userProfile);
 * }
 * </pre>
 *
 * <p><strong>Author:</strong> Konstantin Kokoshnikov</p>
 *
 * @see com.example.authenticationserver.query.api.consistency.UserProfileLookupEntityRepository
 */
package com.example.authenticationserver.query.api.consistency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public final class UserProfileLookupEntity {
  @Id
  private String userId;
  private String userName;
}

