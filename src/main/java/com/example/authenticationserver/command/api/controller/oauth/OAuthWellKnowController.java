package com.example.authenticationserver.command.api.controller.oauth;

import com.project.core.queries.user.FetchJwkSet;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tokenlib.util.jwk.SimpleJWK;
import tokenlib.util.jwk.SimpleJWKSet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/.well-known")
public class OAuthWellKnowController {
    private final QueryGateway queryGateway;
    @Autowired
    public OAuthWellKnowController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/openid-configuration")
    public Map<String, Object> discovery() {
        return createOpenIdConfiguration();
    }
    @GetMapping("/jwks.json")
    public SimpleJWKSet getJson(){
        var query = FetchJwkSet.builder().build();
        return new SimpleJWKSet(queryGateway.query(query, ResponseTypes.multipleInstancesOf(SimpleJWK.class)).join());
    }
    private static Map<String, Object> createOpenIdConfiguration() {
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("issuer", "http://localhost:8080");
        configuration.put("authorization_endpoint", "http://localhost:8080/oauth2/authorize");
        configuration.put("token_endpoint", "http://localhost:8080/oauth2/token");
        configuration.put("userinfo_endpoint", "http://localhost:8080/userinfo");
        configuration.put("jwks_uri", "http://localhost:8080/.well-known/jwks.json");

        List<String> responseTypesSupported = Arrays.asList("code", "token");
        configuration.put("response_types_supported", responseTypesSupported);

        List<String> subjectTypesSupported = Arrays.asList("public");
        configuration.put("subject_types_supported", subjectTypesSupported);

        List<String> idTokenSigningAlgValuesSupported = Arrays.asList("RSA");
        configuration.put("id_token_signing_alg_values_supported", idTokenSigningAlgValuesSupported);

        List<String> scopesSupported = Arrays.asList("openid", "profile", "email");
        configuration.put("scopes_supported", scopesSupported);

        List<String> tokenEndpointAuthMethodsSupported = Arrays.asList("client_secret_basic");
        configuration.put("token_endpoint_auth_methods_supported", tokenEndpointAuthMethodsSupported);

        List<String> claimsSupported = Arrays.asList("sub", "iss", "email", "profile");
        configuration.put("claims_supported", claimsSupported);

        return configuration;
    }
}
