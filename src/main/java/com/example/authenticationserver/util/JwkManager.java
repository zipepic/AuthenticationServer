package com.example.authenticationserver.util;

import com.example.authenticationserver.util.jwk.RSAParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.*;
@Service
public class JwkManager extends TokenProcessor {
  private static final String JWK_FILE_PATH= "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";
  private RSAKey rsaKey;
  @Override
  public Claims extractClaims(String jwtToken) throws Exception {
    String kid = getJwtHeader(jwtToken).getKeyID();
    var jwk = getJWKSet().getKeyByKeyId(kid).toPublicJWK();
    RSAKey rsaKey = RSAParser.parseRSAKeyFromJson(jwk.toJSONString());


    return Jwts.parser()
      .setSigningKey(rsaKey.toRSAPublicKey())
      .parseClaimsJws(jwtToken)
      .getBody();
  }

  @Override
  public String signAndCompactWithDefaults(JwtBuilder jwt) throws Exception {
    //TODO fix regenerating key for access/refresh token
    var keyPair = generateKeyPair();

    var generatedJwt = jwt
      .setIssuer(AppConstants.ISSUER.toString())
      .setIssuedAt(new Date())
      .signWith(keyPair.getPrivate()).compact();

       this.rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
        .keyID(getJwtHeader(generatedJwt).getKeyID())
        .build();
    return generatedJwt;
  }
  @Override
  public JwtBuilder tokenId(JwtBuilder iwt, String tokenId) {
    return iwt.setHeader(Map.of("kid",tokenId));
  }

  @Override
  public Claims tokenId(Claims claims, String tokenId) {
    return claims;
  }

  @Override
  public void save() throws IOException, ParseException {
    ObjectMapper objectMapper = new ObjectMapper();
    JWKSet jwkSet = JWKSet.load(new File(JWK_FILE_PATH));

    List<JWK> keys = new ArrayList<>(jwkSet.getKeys());

    keys.add(this.rsaKey);

    JWKSet updatedJWKSet = new JWKSet(keys);

    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JWK_FILE_PATH), updatedJWKSet.toJSONObject());
  }
  private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);

    return keyPairGenerator.genKeyPair();
  }
  public JWKSet getJWKSet() throws IOException, ParseException {
    return JWKSet.load(new File(JWK_FILE_PATH));
  }
}
