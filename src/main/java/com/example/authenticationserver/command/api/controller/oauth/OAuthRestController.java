package com.example.authenticationserver.command.api.controller.oauth;

import com.example.authenticationserver.query.api.dto.TokenAuthorizationCodeDTO;
import com.example.authenticationserver.util.JwtTokenUtils;
import com.project.core.commands.code.UseAuthorizationCodeCommand;
import com.project.core.queries.app.CheckLoginDataQuery;
import io.jsonwebtoken.Claims;
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
  @Autowired
  public OAuthRestController(QueryGateway queryGateway, CommandGateway commandGateway) {
    this.queryGateway = queryGateway;
    this.commandGateway = commandGateway;
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
  public TokenAuthorizationCodeDTO refreshToken(@RequestParam String client_id,
                                                @RequestParam String client_secret,
                                                @RequestParam String refresh_token){
    return JwtTokenUtils.refresh(refresh_token);
  }
}
