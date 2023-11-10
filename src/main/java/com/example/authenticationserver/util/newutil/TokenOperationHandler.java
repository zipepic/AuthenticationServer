package com.example.authenticationserver.util.newutil;

interface TokenOperationHandler extends TokenRefresher, TokenClaimsExtractor, UserTokenIssuer, TokenSaver {
}
