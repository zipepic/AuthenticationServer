package com.example.authenticationserver.command.api.service;

import com.project.core.dto.TokenAuthorizationCodeDTO;
import com.project.core.dto.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tokenlib.util.TokenFacade;
import tokenlib.util.tokenenum.TokenExpiration;
import tokenlib.util.tokenenum.TokenFields;

import java.util.Map;

@Service
public class TokenManagerService {
    private final TokenFacade tokenFacade;

    @Autowired
    public TokenManagerService(TokenFacade tokenFacade) {
        this.tokenFacade = tokenFacade;
    }
    public TokenDTO makeTokenDTO(Map<String, String> tokenMap, String tokenId) {
        return TokenAuthorizationCodeDTO.builder()
                .accessToken(tokenMap.get("access"))
                .expiresIn((int) TokenExpiration.TEN_MINUTES.getMilliseconds())
                .refreshExpiresIn((int) TokenExpiration.ONE_HOUR.getMilliseconds())
                .refreshToken(tokenMap.get("refresh"))
                .tokenType("Bearer")
                .tokenId(tokenId)
                .build();
    }

    public Map<String, String> generateJwtTokensMap(String userId, String tokenId) {
        try {
            return tokenFacade.issueUserTokens(userId, tokenId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> refreshToken(String refreshToken, String tokenId) {
        try {
            var claims = tokenFacade.extractClaimsFromToken(refreshToken);
            var map = tokenFacade.refreshTokens(claims, tokenId);
            map.put("last_token_id", claims.getId());
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
