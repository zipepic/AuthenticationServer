package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.security.UserProfileDetails;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserProfileController {
  @GetMapping("/profile")
  public String userprofile(@AuthenticationPrincipal UserProfileDetails userProfileDetails){
    return userProfileDetails.getUserProfileEntity().toString();
  }
}
