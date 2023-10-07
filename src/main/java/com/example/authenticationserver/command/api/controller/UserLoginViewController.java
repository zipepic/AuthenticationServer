package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.security.AuthUserProfileProviderImpl;
import com.example.authenticationserver.security.UserProfileDetails;
import com.example.authenticationserver.service.UserProfileDetailsService;
import com.project.core.commands.code.GenerateAuthorizationCodeCommand;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Controller
@RequestMapping("/login")
public class UserLoginViewController {
  //TODO Add the ability to log in without additional parameters
  private final QueryGateway queryGateway;
  private final PasswordEncoder passwordEncoder;
  private final CommandGateway commandGateway;
  private final UserProfileDetailsService userProfileDetailsService;
  private final AuthUserProfileProviderImpl authUserProfileProvider;
  @Autowired
  public UserLoginViewController(QueryGateway queryGateway, PasswordEncoder passwordEncoder, CommandGateway commandGateway, UserProfileDetailsService userProfileDetailsService, AuthUserProfileProviderImpl authUserProfileProvider) {
    this.queryGateway = queryGateway;
    this.passwordEncoder = passwordEncoder;
    this.commandGateway = commandGateway;
    this.userProfileDetailsService = userProfileDetailsService;
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
  @PostMapping
  public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("client_id") String clientId,
                      @RequestParam("response_type") String responseType,
                      @RequestParam("state") String state,
                      @RequestParam("scope") String scope,
                      @RequestParam("redirect_url") String redirectUrl,
                      HttpSession session){
    try {
      var auth = authUserProfileProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));

      UserProfileDetails userProfileDetails = (UserProfileDetails) auth.getPrincipal();

      var codeCommand = GenerateAuthorizationCodeCommand.builder()
          .userId(userProfileDetails.getUserProfileEntity().getUserId())
          .clientId(clientId)
          .scope(scope)
          .sessionId(session.getId())
          .build();

      String code = commandGateway.sendAndWait(codeCommand);

      UriComponentsBuilder builder = UriComponentsBuilder.fromPath(redirectUrl)
        .queryParam("state", state)
        .queryParam("code", code);

      return "redirect:" + builder.toUriString();
    }
    catch (Exception e){
      UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/login")
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
