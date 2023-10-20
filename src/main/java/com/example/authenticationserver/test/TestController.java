package com.example.authenticationserver.test;

import com.example.authenticationserver.util.JwkManager;
import io.jsonwebtoken.Claims;
import net.minidev.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/oauth2/authorization/jwk")
public class TestController {
  private final JwkManager jwtTokenGenerator;

  public TestController(JwkManager jwtTokenGenerator) {
    this.jwtTokenGenerator = jwtTokenGenerator;
  }

  @PostMapping("/token")
  public String getToken(@RequestParam String userId) throws Exception {
    String kid = UUID.randomUUID().toString();
    return JwkManager.generateJwtTokens(userId, kid).get("refresh");
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
