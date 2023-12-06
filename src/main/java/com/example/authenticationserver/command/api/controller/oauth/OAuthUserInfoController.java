package com.example.authenticationserver.command.api.controller.oauth;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthUserInfoController {
    @GetMapping("/userinfo")
    public Object userInfo(){
        //TODO implement this with open id connect, available scopes are: openid, profile,
        return null;
    }
}
