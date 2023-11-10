//package com.example.authenticationserver.util;
//
//import com.example.authenticationserver.util.jwk.AppConstants;
//import com.example.authenticationserver.util.jwk.KeyContainer;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import io.jsonwebtoken.Jwts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.spec.SecretKeySpec;
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.Date;
//
//@Service
//public class JwtManager extends TokenProcessor implements ManagerImplementator {
//  private static SecretKeySpec secretKey;
//  @Autowired
//  public JwtManager(SecretKeySpec secretKey) {
//    this.secretKey = secretKey;
//  }
//
//  @Override
//  public Claims extractClaims(String jwtToken) throws Exception {
//    return Jwts.parser()
//      .setSigningKey(this.keyContainer.getVerifyKey())
//      .parseClaimsJws(jwtToken)
//      .getBody();
//  }
//  @Override
//  public String signAndCompactWithDefaults(JwtBuilder jwt) {
//    return jwt
//      .setIssuer(AppConstants.ISSUER.toString())
//      .setIssuedAt(new Date())
//      .signWith(keyContainer.getSignKey()).compact();
//  }
//
//  @Override
//  public void save(String userId) throws IOException, ParseException {
//
//  }
//
//  @Override
//  public KeyContainer getKeyContainer() {
//    return new KeyContainer(secretKey);
//  }
//
//}
