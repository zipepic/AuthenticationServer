package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.ApplicationLoginRestModel;
import com.example.authenticationserver.command.api.restmodel.ApplicationRegistrationRestModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class ApplicationController {
  @PostMapping("/register")
  public String register(@RequestBody ApplicationRegistrationRestModel model){
    return "";
  }
  @PostMapping("/login")
  public String login(@RequestBody ApplicationLoginRestModel model){
    return "";
  }
}
