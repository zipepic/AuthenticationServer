package com.example.authenticationserver.utils;

import com.example.authenticationserver.util.newutils.JwkManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JwkManagerTest {
  private static final String JWK_FILE_PATH= "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";
  private JwkManager jwkManager;
  private Map<String,String> tokens;
  @Before
  public void setUp() throws Exception {
    this.jwkManager = new JwkManager();
    this.tokens = jwkManager.generateJwtTokens("userId", "tokenId");
  }
  @Test
  public void checkGenerateToken() throws Exception {

    var jwks = jwkManager.getJWKSet();
    Assert.assertNotEquals(new JWKSet().toJSONObject(), jwks.toJSONObject());
    Assert.assertEquals(1,jwks.getKeys().size());
  }
  @Test
  public void checkExtractClaims() throws Exception {
    var claims = jwkManager.extractClaims(tokens.get("access"));
    Assert.assertEquals("userId",claims.getSubject());
  }
  @After
  public void tearDown() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JWK_FILE_PATH), new JWKSet().toJSONObject());
  }
}
