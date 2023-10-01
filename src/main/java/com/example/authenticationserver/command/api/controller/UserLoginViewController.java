package com.example.authenticationserver.command.api.controller;

import com.project.core.commands.user.GenerateOneTimeCodeUserProfileCommand;
import com.project.core.queries.user.FindUserIdByUserNameQuery;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/login")
public class UserLoginViewController {
  private final QueryGateway queryGateway;
  private final PasswordEncoder passwordEncoder;
  private final CommandGateway commandGateway;
  @Autowired
  public UserLoginViewController(QueryGateway queryGateway, PasswordEncoder passwordEncoder, CommandGateway commandGateway) {
    this.queryGateway = queryGateway;
    this.passwordEncoder = passwordEncoder;
    this.commandGateway = commandGateway;
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
                      HttpServletResponse response) {

    FindUserIdByUserNameQuery query =
      FindUserIdByUserNameQuery.builder()
        .userName(username)
        .build();

    String userId = queryGateway.query(query, String.class).join();

    GenerateOneTimeCodeUserProfileCommand command =
      GenerateOneTimeCodeUserProfileCommand.builder()
        .userId(userId)
        .passwordHash(password)
        .build();

    try {
      String code = commandGateway.sendAndWait(command);
      return "redirect:" + redirectUrl + "?state=" + state + "&code=" + code;
    }
    catch (Exception e){
      return "login";
    }

  }
}
