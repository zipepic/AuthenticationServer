package com.example.authenticationserver.utils;

import com.example.authenticationserver.util.newutils.JwkManager;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

public class JwkManagerTest {
  private JwkManager jwkManager;

  @Before
  public void setUp(){
    this.jwkManager = new JwkManager();
  }
  @Test
  public void сheckingThatTheKeyFileIsEmptyBeforeStartingTests() throws IOException, ParseException {
    Assert.assertEquals(new JWKSet().toJSONObject(), jwkManager.getJWKSet().toJSONObject());
  }
  @Test
  public void test() throws Exception {
    var header = jwkManager.getJwtHeader(jwkManager.generateJwtTokens("userId", "tokenId").get("refresh"));
    Assert.assertEquals("tokenId", header.getKeyID());
  }
}
