package com.example.authenticationserver.test;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/api")
public class TestController {
  private final JwtTokenGenerator jwtTokenGenerator;

  public TestController(JwtTokenGenerator jwtTokenGenerator) {
    this.jwtTokenGenerator = jwtTokenGenerator;
  }

  @GetMapping("/token")
  public String getToken() throws Exception {
    return jwtTokenGenerator.generateJwtToken();
  }
  @PostMapping("/validate")
  public Claims validateToken(@RequestParam String jwt) throws Exception {
    return jwtTokenGenerator.verifyAndParseJWT(jwt);
  }

}
