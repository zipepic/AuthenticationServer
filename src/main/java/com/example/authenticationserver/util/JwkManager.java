package com.example.authenticationserver.util;

import com.example.authenticationserver.util.jwk.AppConstants;
import com.example.authenticationserver.util.jwk.KeyContainer;
import com.example.authenticationserver.util.jwk.RSAParser;
import com.example.authenticationserver.util.tokenenum.TokenFields;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.project.core.events.user.JwkTokenInfoEvent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.io.File;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwkManager implements JwkProvider{
  private static final String JWK_FILE_PATH = "/Users/xzz1p/Documents/MySpring/TEST_PROJECT/AuthenticationServer/jwk.json";

  @Override
  public Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException {
    String kid = extractKidFromTokenHeader(jwtToken);
    var jwk = loadJwkSetFromSource().getKeyByKeyId(kid).toPublicJWK();
    RSAKey rsaKey = RSAParser.parseRSAKeyFromJson(jwk.toJSONString());

    return Jwts.parser()
      .setSigningKey(rsaKey.toRSAPublicKey())
      .parseClaimsJws(jwtToken)
      .getBody().setId(kid);
  }


  @Override
  public String generateSignedCompactToken(JwtBuilder jwt,String kid) {

    String generatedJwt = null;
    try {
      generatedJwt = jwt
        .setIssuer(AppConstants.ISSUER.toString())
        .setIssuedAt(new Date())
        .setHeader(Map.of(TokenFields.KID.getValue(), kid))
        .signWith(obtainKeyContainer().getSignKey()).compact();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return generatedJwt;
  }

  @Override
  public Map<String, String> generateTokenPair(JwtBuilder access, JwtBuilder refresh, String tokenId) throws NoSuchAlgorithmException {
    var keyContainer = obtainKeyContainer();
    var rsaKey = new RSAKey.Builder((RSAPublicKey) keyContainer.getVerifyKey())
      .keyID(tokenId)
      .build();
    Map<String, String> tokenMap = new HashMap<>();
    tokenMap.put("refresh", generateSignedCompactToken(refresh,tokenId,keyContainer));
    tokenMap.put("access", generateSignedCompactToken(access,tokenId, keyContainer));
    tokenMap.put(TokenFields.PUBLIC_KEY.getValue(), rsaKey.toJSONString());
    return tokenMap;
  }

  @Override
  public KeyContainer obtainKeyContainer() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);

    return new KeyContainer(keyPairGenerator.generateKeyPair());
  }

  @Override
  public JWKSet loadJwkSetFromSource() throws IOException, ParseException {
    return JWKSet.load(new File(JWK_FILE_PATH)).toPublicJWKSet();
  }

  @Override
  public String extractKidFromTokenHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader().getKeyID();
  }

  @Override
  public String generateSignedCompactToken(JwtBuilder jwt, String kid, KeyContainer keyContainer) {
    return  jwt
        .setIssuer(AppConstants.ISSUER.toString())
        .setIssuedAt(new Date())
        .setHeader(Map.of(TokenFields.KID.getValue(),kid))
        .signWith(keyContainer.getSignKey()).compact();
  }
  @Override
  public Class<JwkTokenInfoEvent> getEventClass() {
    return JwkTokenInfoEvent.class;
  }
}
