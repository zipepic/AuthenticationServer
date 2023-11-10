//package com.example.authenticationserver.util;
//
//import com.example.authenticationserver.util.jwk.AppConstants;
//import com.example.authenticationserver.util.jwk.KeyContainer;
//import com.example.authenticationserver.util.jwk.RSAParser;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nimbusds.jose.jwk.JWK;
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.io.IOException;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.interfaces.RSAPublicKey;
//import java.text.ParseException;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class JwkManager extends TokenProcessor implements ManagerImplementator {
//  private static final String JWK_FILE_PATH= "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";
//  @Override
//  public Claims extractClaims(String jwtToken) throws Exception {
//    String kid = getJwtHeader(jwtToken).getKeyID();
//    var jwk = getJWKSet().getKeyByKeyId(kid).toPublicJWK();
//    RSAKey rsaKey = RSAParser.parseRSAKeyFromJson(jwk.toJSONString());
//
//    return Jwts.parser()
//      .setSigningKey(rsaKey.toRSAPublicKey())
//      .parseClaimsJws(jwtToken)
//      .getBody().setId(kid);
//  }
//
//  @Override
//  public String signAndCompactWithDefaults(JwtBuilder jwt) throws Exception {
//    var generatedJwt = jwt
//      .setIssuer(AppConstants.ISSUER.toString())
//      .setIssuedAt(new Date())
//      .setHeader(Map.of("kid",this.tokenId))
//      .signWith(this.keyContainer.getSignKey()).compact();
//    return generatedJwt;
//  }
//  @Override
//  public void save(String userId) throws IOException, ParseException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    JWKSet jwkSet = JWKSet.load(new File(JWK_FILE_PATH));
//
//    var rsaKey = new RSAKey.Builder((RSAPublicKey) this.keyContainer.getVerifyKey())
//      .keyID(this.tokenId)
//      .build();
//
//    List<JWK> keys = new ArrayList<>(jwkSet.getKeys());
//
//    keys = keys.stream().filter(key -> !key.getKeyID().equals(this.tokenId)).collect(Collectors.toList());
//
//    keys.add(rsaKey);
//
//    JWKSet updatedJWKSet = new JWKSet(keys);
//
//    objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(JWK_FILE_PATH), updatedJWKSet.toJSONObject());
//  }
//
//  @Override
//  public KeyContainer getKeyContainer() throws NoSuchAlgorithmException {
//    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//    keyPairGenerator.initialize(2048);
//
//    return new KeyContainer(keyPairGenerator.generateKeyPair());
//  }
//  public JWKSet getJWKSet() throws IOException, ParseException {
//    return JWKSet.load(new File(JWK_FILE_PATH));
//  }
//}
