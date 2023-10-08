package com.example.authenticationserver.command.api.controller.oauth;

import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import com.example.authenticationserver.security.UserProfileDetails;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import jakarta.servlet.http.HttpSession;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/login/oauth2/authorization")
public class OAuthLoginViewController {
  private final CommandGateway commandGateway;
  private final AuthUserProfileProviderImpl authUserProfileProvider;
  @Autowired
  public OAuthLoginViewController(CommandGateway commandGateway, AuthUserProfileProviderImpl authUserProfileProvider) {
    this.commandGateway = commandGateway;
    this.authUserProfileProvider = authUserProfileProvider;
  }

  @GetMapping
  public String showLoginForm(@RequestParam("client_id") String clientId,
                              @RequestParam("response_type") String responseType,
                              @RequestParam("state") String state,
                              @RequestParam("scope") String scope,
                              @RequestParam("redirect_url") String redirectUrl,
                              Model model) {
    model.addAttribute("client_id", clientId);
    model.addAttribute("response_type", responseType);
    model.addAttribute("state", state);
    model.addAttribute("scope", scope);
    model.addAttribute("redirect_url", redirectUrl);

    return "login";
  }
  @PostMapping(params = {"username", "password", "client_id", "response_type", "state", "scope", "redirect_url"})
  public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("client_id") String clientId,
                      @RequestParam("response_type") String responseType,
                      @RequestParam("state") String state,
                      @RequestParam("scope") String scope,
                      @RequestParam("redirect_url") String redirectUrl,
                      HttpSession session) {
    try {
      var auth = authUserProfileProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));

      UserProfileDetails userProfileDetails = (UserProfileDetails) auth.getPrincipal();

      var codeCommand = GenerateAuthorizationCodeCommand.builder()
        .userId(userProfileDetails.getUserProfileEntity().getUserId())
        .clientId(clientId)
        .scope(scope)
        .sessionId(session.getId())
        .build();

      CompletableFuture<String> code = commandGateway.send(codeCommand);

      UriComponentsBuilder builder = UriComponentsBuilder.fromPath(redirectUrl)
        .queryParam("state", state)
        .queryParam("code", code.get());

      return "redirect:" + builder.toUriString();
    } catch (Exception e) {
      UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/login/oauth2/authorization")
        .queryParam("client_id", clientId)
        .queryParam("response_type", responseType)
        .queryParam("state", state)
        .queryParam("scope", scope)
        .queryParam("redirect_url", redirectUrl)
        .queryParam("error", "Invalid username or password");

      return "redirect:" + builder.toUriString();
    }
  }
}
