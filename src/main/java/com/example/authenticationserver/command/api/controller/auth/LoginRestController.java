package com.example.authenticationserver.command.api.controller.auth;

import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import com.project.core.dto.TokenDTO;
import com.example.authenticationserver.security.UserProfileDetails;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginRestController {
  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;
  private final UserProfileCommandService userProfileCommandService;
  @Autowired
  public LoginRestController(CommandGateway commandGateway, QueryGateway queryGateway, UserProfileCommandService userProfileCommandService) {
    this.commandGateway = commandGateway;
    this.queryGateway = queryGateway;
    this.userProfileCommandService = userProfileCommandService;
  }
  @PostMapping("/login")
  public ResponseEntity<TokenDTO> login(@RequestParam String username,
                                        @RequestParam String password,
                                        @RequestParam String tokenType){
    var userDetails = userProfileCommandService.authenticationUser(new UsernamePasswordAuthenticationToken(username,password));
    if(true)
        throw new RuntimeException("Unsupoorted method");
    return null;
  }
  @GetMapping("/profile")
  public String profile(@AuthenticationPrincipal UserProfileDetails userProfileDetails){
    return userProfileDetails.getUserProfileEntity().toString();
  }
}
