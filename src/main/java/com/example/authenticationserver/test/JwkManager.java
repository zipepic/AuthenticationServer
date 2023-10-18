package com.example.authenticationserver.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.*;

@Service
public class JwkManager {
  private final JwksRepository jwksRepository;
  private static final String JWK_FILE_PATH= "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";

  public JwkManager(JwksRepository jwksRepository) {
    this.jwksRepository = jwksRepository;
  }

  public String generateJwtToken() throws Exception {
    String kid = UUID.randomUUID().toString();

    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);

    KeyPair keyPair = keyPairGenerator.genKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();

    String jwtToken = Jwts.builder()
      .setHeaderParam("kid", kid)
      .setSubject("exampleUser")
      .setIssuer("exampleIssuer")
      .setAudience("exampleAudience")
      .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Срок действия 1 час
      .setIssuedAt(new Date())
      .claim("customClaim", "customClaimValue")
      .signWith(privateKey, SignatureAlgorithm.RS256)
      .compact();

    RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
      .keyID(kid)
      .build();

    saveInJsonFile(rsaKey);
    return jwtToken;
    //    jwksRepository.save(new JwksEntity(kid, jwkSet.getKeyByKeyId(kid).toJSONString()));
  }
  public Claims verifyAndParseJWT(String jwtToken) throws Exception {

    String kid = getJwtHeader(jwtToken).getKeyID();
    var jwk = getJWKSet().getKeyByKeyId(kid).toPublicJWK();
    RSAKey rsaKey = RSAParser.parseRSAKeyFromJson(jwk.toJSONString());


    return Jwts.parser()
      .setSigningKey(rsaKey.toRSAPublicKey())
      .parseClaimsJws(jwtToken)
      .getBody();
  }
  public JWSHeader getJwtHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader();
  }
  private void saveInJsonFile(RSAKey rsaKey) throws IOException, ParseException {

    ObjectMapper objectMapper = new ObjectMapper();
    JWKSet jwkSet = JWKSet.load(new File(JWK_FILE_PATH));

    List<JWK> keys = new ArrayList<>(jwkSet.getKeys());

    keys.add(rsaKey);

    JWKSet updatedJWKSet = new JWKSet(keys);

    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JWK_FILE_PATH), updatedJWKSet.toJSONObject());
  }
  public JWKSet getJWKSet() throws IOException, ParseException {
    return JWKSet.load(new File(JWK_FILE_PATH));
  }
}
