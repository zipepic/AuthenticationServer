//package com.example.authenticationserver.utils;
//
//import static org.junit.Assert.*;
//
//import com.example.authenticationserver.util.TokenProcessor;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtBuilder;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.security.NoSuchAlgorithmException;
//import java.text.ParseException;
//import java.util.Map;
//
//public class TokenProcessorTest extends TokenProcessor {
//
//  @Before
//  public void setUp() throws Exception {
//    // Здесь вы можете выполнять начальную настройку для тестов, если это необходимо.
//  }
//
//  @Test
//  public void testIsRefreshToken() {
//    Claims claims = Jwts.claims().put("token_type", "refresh_token");
//    assertTrue(isRefreshToken(claims));
//
//    Claims claims2 = Jwts.claims().put("token_type", "access_token");
//    assertFalse(isRefreshToken(claims2));
//  }
//
//  @Test
//  public void testSignAndCompactWithDefaults() {
//    // Здесь можно написать тесты для метода signAndCompactWithDefaults,
//    // проверяя, что он правильно создает JWT и возвращает корректный результат.
//  }
//
//  @Test
//  public void testGenerateTokenWithClaims() {
//    // Здесь можно написать тесты для метода generateTokenWithClaims,
//    // проверяя, что он правильно создает токен с заданными утверждениями.
//  }
//
//  @Test
//  public void testRefresh() {
//    // Здесь можно написать тесты для метода refresh,
//    // проверяя, что он правильно обновляет токены.
//  }
//
//  // Дополнительные тесты для других методов TokenProcessor
//
//  @Test
//  public void testGetJwtHeader() {
//    // Здесь можно написать тесты для метода getJwtHeader, если он не абстрактный.
//  }
//
//  @Override
//  public Claims extractClaims(String jwtToken) throws Exception {
//    return null;
//  }
//
//  @Override
//  public String signAndCompactWithDefaults(JwtBuilder jwt) throws NoSuchAlgorithmException, IOException, ParseException {
//    return null;
//  }
//
//  @Override
//  public JwtBuilder tokenId(JwtBuilder iwt, String tokenId) {
//    return iwt.setId(tokenId);
//  }
//
//  @Override
//  public Claims tokenId(Claims claims, String tokenId) {
//    return claims.setId(tokenId);
//  }
//}
//
