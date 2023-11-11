package com.example.authenticationserver.util.newutil;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

interface UserTokenIssuer {
  Map<String, String> issueUserTokens(String userId, String tokenId) throws NoSuchAlgorithmException;
}
