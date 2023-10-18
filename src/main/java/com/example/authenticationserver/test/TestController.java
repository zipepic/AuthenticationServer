package com.example.authenticationserver.test;

import com.nimbusds.jose.jwk.JWKSet;
import io.jsonwebtoken.Claims;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2/authorization/jwk")
public class TestController {
  private final JwkManager jwtTokenGenerator;

  public TestController(JwkManager jwtTokenGenerator) {
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

  @GetMapping("/jwk.json")
  public JSONObject getJWKSet() throws Exception {
    return jwtTokenGenerator.getJWKSet().toJSONObject();
  }
}
