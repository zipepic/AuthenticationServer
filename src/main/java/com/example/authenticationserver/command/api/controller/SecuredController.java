package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecuredController {
  private final UserProfileCommandService userProfileCommandService;
  @Autowired
  public SecuredController(UserProfileCommandService userProfileCommandService) {
    this.userProfileCommandService = userProfileCommandService;
  }

  @GetMapping("/user")
  public String user(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    userProfileCommandService.on();
    return authentication.getName();
  }
  @GetMapping("/profile")
  public String userprofile(){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }
}
