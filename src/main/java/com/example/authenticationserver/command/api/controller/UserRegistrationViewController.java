package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import com.project.core.commands.user.CreateUserProfileCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
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

import java.util.UUID;
@Controller
@RequestMapping("/registration")
public class UserRegistrationViewController {
  private final UserProfileCommandService userProfileCommandService;
  @Autowired
  public UserRegistrationViewController(UserProfileCommandService userProfileCommandService) {
    this.userProfileCommandService = userProfileCommandService;
  }

  @GetMapping
  public String showRegistrationForm(@RequestParam("client_id") String clientId,
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

    return "registration";
  }
  @PostMapping
  public String create(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam("client_id") String clientId,
                       @RequestParam("response_type") String responseType,
                       @RequestParam("state") String state,
                       @RequestParam("scope") String scope,
                       @RequestParam("redirect_url") String redirectUrl) {

    userProfileCommandService.register(new UsernamePasswordAuthenticationToken(username, password)).join();

    UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/login")
      .queryParam("client_id", clientId)
      .queryParam("response_type", responseType)
      .queryParam("state", state)
      .queryParam("scope", scope)
      .queryParam("redirect_url", redirectUrl);

    return "redirect:" + builder.toUriString();
  }
}
