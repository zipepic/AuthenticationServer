package com.example.authenticationserver.util.newutil;

import java.util.Map;

interface UserTokenIssuer {
  Map<String, String> issueUserTokens(String userId, String tokenId);
}
