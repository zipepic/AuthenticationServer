package com.example.authenticationserver.command.api.controller.oauth;

import com.project.core.dto.TokenAuthorizationCodeDTO;
import com.project.core.dto.TokenDTO;
import com.project.core.dto.TokenSummary;
import tokenlib.util.jwk.AppConstants;
import tokenlib.util.TokenFacade;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.queries.app.CheckLoginDataQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/authorization")
public class OAuthRestController {
  private final QueryGateway queryGateway;
  private final CommandGateway commandGateway;
  private final TokenFacade jwtManager;
  @Autowired
  public OAuthRestController(QueryGateway queryGateway, CommandGateway commandGateway, TokenFacade jwtManager) {
    this.queryGateway = queryGateway;
    this.commandGateway = commandGateway;
    this.jwtManager = jwtManager;
  }

  @PostMapping("/token")
  public TokenAuthorizationCodeDTO generateTokens(@RequestParam String grant_type,
                                                  @RequestParam String client_id,
                                                  @RequestParam String client_secret,
                                                  @RequestParam String code,
                                                  @RequestParam String redirect_uri){

    CheckLoginDataQuery loginDataQuery =
      CheckLoginDataQuery.builder()
        .clientId(client_id)
        .secret(client_secret)
        .build();

    boolean applicationIsPresent = queryGateway.query(loginDataQuery, Boolean.class).join();
    if(!applicationIsPresent)
      throw new RuntimeException("Application is not present");

    UseAuthorizationCodeCommand command =
      UseAuthorizationCodeCommand.builder()
        .code(code)
        .build();

    return commandGateway.sendAndWait(command);
  }
  @PostMapping("/refresh_token")
  public TokenDTO refreshToken(@RequestParam String client_id,
                               @RequestParam String client_secret,
                               @RequestParam String refresh_token){
    try {

      var claims = jwtManager.extractClaimsFromToken(refresh_token);
      var tokenMap = jwtManager.refreshTokens(claims, claims.getId());
      return TokenSummary.builder()
        .accessToken(tokenMap.get("access"))
        .expiresIn(AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())
        .refreshExpiresIn(AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())
        .refreshToken(tokenMap.get("refresh"))
        .tokenType("Bearer")
        .tokenId(claims.getId())
        .build();
    }catch (Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }
}
