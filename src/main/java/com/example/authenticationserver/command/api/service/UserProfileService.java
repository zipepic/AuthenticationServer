package com.example.authenticationserver.command.api.service;

/**
 * The {@code UserProfileService} class provides functionality for handling user profile-related operations,
 * such as token generation and refresh, within the authentication server.
 *
 * <p><strong>Dependencies:</strong></p>
 * <ul>
 *   <li>{@code TokenFacade}:
 *       A facade for token-related operations provided by the external token library.</li>
 *   <li>{@code TokenAuthorizationCodeDTO}:
 *       Data transfer object representing the details of an authorization code token.</li>
 *   <li>{@code JwtTokenInfoEvent}:
 *       Event representing information about a JSON Web Token (JWT).</li>
 *   <li>{@code JwkTokenInfoEvent}:
 *       Event representing information about a JSON Web Key (JWK).</li>
 *   <li>{@code TokenExpiration}:
 *       Enumeration representing different token expiration periods.</li>
 *   <li>{@code TokenFields}:
 *       Enumeration representing various fields associated with tokens.</li>
 * </ul>
 *
 * <p><strong>Usage:</strong></p>
 * <p>The {@code UserProfileService} is used by the {@code UserProfileAggregate} to handle token-related operations
 * for user profiles within the authentication server.</p>
 *
 * <p><strong>Author:</strong> XZZ1P</p>
 *
 * <p><strong>See Also:</strong></p>
 * <ul>
 *   <li>{@link com.example.authenticationserver.command.api.aggregate.UserProfileAggregate}</li>
 *   <li>{@link com.example.authenticationserver.dto.TokenAuthorizationCodeDTO}</li>
 *   <li>{@link com.project.core.events.user.JwkTokenInfoEvent}</li>
 *   <li>{@link com.project.core.events.user.JwtTokenInfoEvent}</li>
 *   <li>{@link tokenlib.util.TokenFacade}</li>
 *   <li>{@link tokenlib.util.tokenenum.TokenExpiration}</li>
 *   <li>{@link tokenlib.util.tokenenum.TokenFields}</li>
 * </ul>
 */
import com.example.authenticationserver.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.dto.TokenDTO;
import com.project.core.events.user.JwkTokenInfoEvent;
import com.project.core.events.user.JwtTokenInfoEvent;
import org.springframework.context.annotation.Scope;
import tokenlib.util.TokenFacade;
import tokenlib.util.tokenenum.TokenExpiration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenlib.util.tokenenum.TokenFields;

import java.util.Map;

@Service
@Scope("prototype")
public class UserProfileService {
  private final TokenFacade tokenFacade;

  /**
   * Constructor for UserProfileService.
   *
   * @param tokenFacade The {@code TokenFacade} providing token-related operations.
   */
  @Autowired
  public UserProfileService(TokenFacade tokenFacade) {
    this.tokenFacade = tokenFacade;
  }

  /**
   * Generates a {@code TokenDTO} based on the provided token map and token ID.
   *
   * @param tokenMap The map containing token information.
   * @param tokenId  The ID of the token.
   * @return The {@code TokenDTO} representing the generated tokens.
   */
  public TokenDTO makeTokenDTO(Map<String, String> tokenMap, String tokenId) {
    return TokenAuthorizationCodeDTO.builder()
      .accessToken(tokenMap.get("access"))
      .expiresIn((int) TokenExpiration.TEN_MINUTES.getMilliseconds())
      .refreshExpiresIn((int) TokenExpiration.ONE_HOUR.getMilliseconds())
      .refreshToken(tokenMap.get("refresh"))
      .tokenType("Bearer")
      .tokenId(tokenId)
      .build();
  }

  /**
   * Generates JWT tokens map for the given user ID and token ID.
   *
   * @param userId  The user ID.
   * @param tokenId The ID of the token.
   * @return The map containing JWT tokens.
   */
  public Map<String, String> generateJwtTokensMap(String userId, String tokenId) {
    try {
      return tokenFacade.issueUserTokens(userId, tokenId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Refreshes tokens based on the provided refresh token and token ID.
   *
   * @param refreshToken The refresh token.
   * @param tokenId      The ID of the token.
   * @return The map containing refreshed tokens.
   */
  public Map<String, String> refreshToken(String refreshToken, String tokenId) {
    try {
      var claims = tokenFacade.extractClaimsFromToken(refreshToken);
      var map = tokenFacade.refreshTokens(claims, tokenId);
      map.put("last_token_id", claims.getId());
      return map;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets the class of the event associated with the token.
   *
   * @return The event class.
   */
  public Class<?> getEventClass() {
    return tokenFacade.getEventClass();
  }

  /**
   * Handles token information event based on the user ID, token map, and token ID.
   *
   * @param userId  The user ID.
   * @param map     The map containing token information.
   * @param tokenId The ID of the token.
   * @return The appropriate token information event.
   */
  public Object handleTokenInfoEvent(String userId, Map<String, String> map, String tokenId) {
    var eventClass = getEventClass();
    if (eventClass.equals(JwkTokenInfoEvent.class)) {
      var jwkTokenInfoEvent = JwkTokenInfoEvent.builder()
        .userId(userId)
        .publicKey(map.get(TokenFields.PUBLIC_KEY.getValue()))
        .kid(tokenId.toString())
        .lastTokenId(map.get("last_token_id"))
        .build();
      return jwkTokenInfoEvent;
    } else if (eventClass.equals(JwtTokenInfoEvent.class)) {
      var jwtTokenInfoEvent = JwtTokenInfoEvent.builder()
        .userId(userId)
        .tokenId(tokenId.toString())
        .build();
      return jwtTokenInfoEvent;
    }
    throw new IllegalArgumentException("Invalid event class");
  }
}
