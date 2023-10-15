package com.example.authenticationserver.command.api.test;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtTokenGenerator {
  private final JwksRepository jwksRepository;

  public JwtTokenGenerator(JwksRepository jwksRepository) {
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

    JWKSet jwkSet = new JWKSet(rsaKey);
    System.out.println(jwkSet);

    jwksRepository.save(new JwksEntity(kid, jwkSet.getKeyByKeyId(kid).toJSONString()));
    return jwtToken;
  }
  public Claims verifyAndParseJWT(String jwtToken) throws Exception {

    String kid = getJwtHeader(jwtToken).getKeyID();

    Optional<JwksEntity> optionalJwksEntity = jwksRepository.findById(kid);
    if (optionalJwksEntity.isEmpty()) {
      throw new IllegalArgumentException("Token not found");
    }
    var jwksEntity = optionalJwksEntity.get();
    RSAKey rsaKey = RSAParser.parseRSAKeyFromJson(jwksEntity.getJwksData());


    return Jwts.parser()
      .setSigningKey(rsaKey.toPublicKey())
      .parseClaimsJws(jwtToken)
      .getBody();
  }
  public JWSHeader getJwtHeader(String jwtToken) throws ParseException {
    JWSObject jwsObject = JWSObject.parse(jwtToken);
    return jwsObject.getHeader();
  }
}
