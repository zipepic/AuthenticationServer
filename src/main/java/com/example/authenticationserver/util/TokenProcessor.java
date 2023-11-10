//package com.example.authenticationserver.util;
//
//import com.example.authenticationserver.util.jwk.AppConstants;
//import com.example.authenticationserver.util.jwk.KeyContainer;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSObject;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//
//import java.text.ParseException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Абстрактный класс для работы с токенами
// * @author xzz1p
// * @see TokenUtils
// * @see JwtManager
// * @see KeyContainer
// * @since version 1.0
// */
//
//abstract class TokenProcessor implements TokenUtils {
//  protected KeyContainer keyContainer;
//  protected String tokenId;
//  protected ManagerImplementator managerImplementator;
//  public boolean isRefreshToken(Claims claims) {
//    String tokenType = (String) claims.get("token_type");
//    return "refresh_token".equals(tokenType);
//  }
//  public JWSHeader getJwtHeader(String jwtToken) throws ParseException {
//    JWSObject jwsObject = JWSObject.parse(jwtToken);
//    return jwsObject.getHeader();
//  }
//  public Map<String, String> templateMethodGenerateJwtTokens(String userId, String tokenId) throws Exception{
//    this.keyContainer = managerImplementator.getKeyContainer();
//    this.tokenId = tokenId;
//    var refresh = Jwts.builder()
//      .setSubject(userId)
//      .setIssuer(AppConstants.ISSUER.toString())
//      .setExpiration(new Date(System.currentTimeMillis() + 100000)) // Срок действия 1 час
//      .setIssuedAt(new Date())
//      .addClaims(Map.of("token_type","refresh_token"));
//
//    var access = Jwts.builder()
//      .setSubject(userId)
//      .setExpiration(new Date(System.currentTimeMillis() + 100000)) // Срок действия 10 min
//      .addClaims(Map.of("token_type","access_token"));
//
//    Map<String, String> tokenMap = new HashMap<>();
//    tokenMap.put("refresh", managerImplementator.signAndCompactWithDefaults(refresh));
//    tokenMap.put("access", managerImplementator.signAndCompactWithDefaults(access));
//    managerImplementator.save(userId);
//    return tokenMap;
//  }
//  public String generateTokenWithClaims(Claims claims) throws Exception {
//    var jwt = Jwts.builder()
//      .setClaims(claims);
//    return managerImplementator.signAndCompactWithDefaults(jwt);
//  }
//  public Map<String, String> templateMethodRefreshJwtTokens(Claims claims, String tokenId) throws Exception {
//    this.tokenId = tokenId;
//    if(!isRefreshToken(claims))
//      throw new IllegalArgumentException("Invalid token");
//
//    Claims refreshTokenClaims = Jwts.claims();
//    refreshTokenClaims.putAll(claims);
//
//    refreshTokenClaims
//      .setExpiration(new Date(System.currentTimeMillis() + 100000));
//
//    Claims accessTokenClaims = Jwts.claims();
//    accessTokenClaims.putAll(claims);
//
//    accessTokenClaims
//      .setExpiration(new Date(System.currentTimeMillis() + 100000))
//      .put("token_type", "access_token");
//
//    Map<String, String> tokenMap = new HashMap<>();
//    tokenMap.put("refresh", generateTokenWithClaims(refreshTokenClaims));
//    tokenMap.put("access", generateTokenWithClaims(accessTokenClaims));
//    managerImplementator.save(claims.getSubject());
//    return tokenMap;
//  }
//}
//
