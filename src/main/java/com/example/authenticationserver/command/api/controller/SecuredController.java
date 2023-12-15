package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.service.UserProfileCommandService;
import com.example.authenticationserver.security.UserProfileDetails;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecuredController {
  private final QueryGateway queryGateway;
  private final UserProfileCommandService userProfileCommandService;
  @Autowired
  public SecuredController(QueryGateway queryGateway, UserProfileCommandService userProfileCommandService) {
    this.queryGateway = queryGateway;
    this.userProfileCommandService = userProfileCommandService;
  }

  @GetMapping("/profile")
  public String userprofile(@AuthenticationPrincipal UserProfileDetails userProfileDetails){
    return userProfileDetails.getUserProfileEntity().toString();
  }
  @PostMapping("/validate-refresh-token")
  public ResponseEntity validateRefreshToken(){
    return ResponseEntity.ok().build();
  }
}
