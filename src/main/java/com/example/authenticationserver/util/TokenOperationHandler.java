package com.example.authenticationserver.util;

interface TokenOperationHandler extends TokenRefresher, TokenClaimsExtractor, UserTokenIssuer, EventClassProvider {
}
