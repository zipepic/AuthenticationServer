package com.example.authenticationserver.util;

import com.example.authenticationserver.test.RSAParser;
import com.example.authenticationserver.util.AppConstants;
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
  private static final String JWK_FILE_PATH= "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";

  public static Map<String, String> generateJwtTokens(String userId, String kid) throws Exception {

    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);

    KeyPair keyPair = keyPairGenerator.genKeyPair();
    PrivateKey privateKey = keyPair.getPrivate();

    String refresh = Jwts.builder()
      .setHeaderParam("kid", kid)
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.REFRESH_TOKEN_EXP_TIME.ordinal())) // Срок действия 1 час
      .setIssuedAt(new Date())
      .addClaims(Map.of("token_type","refresh_token"))
      .signWith(privateKey, SignatureAlgorithm.RS256)
      .compact();

    String access = Jwts.builder()
      .setHeaderParam("kid", kid)
      .setSubject(userId)
      .setIssuer(AppConstants.ISSUER.toString())
      .setExpiration(new Date(System.currentTimeMillis() + AppConstants.ACCESS_TOKEN_EXP_TIME.ordinal())) // Срок действия 10 min
      .setIssuedAt(new Date())
      .addClaims(Map.of("token_type","access_token"))
      .signWith(privateKey, SignatureAlgorithm.RS256)
      .compact();

    RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
      .keyID(kid)
      .build();

    saveInJsonFile(rsaKey);

    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", refresh);
    tokenMap.put("access", access);

    return tokenMap;
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
  private static void saveInJsonFile(RSAKey rsaKey) throws IOException, ParseException {

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
