package com.example.authenticationserver.util;

import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.text.ParseException;

interface TokenClaimsExtractor {
  Claims extractClaimsFromToken(String jwtToken) throws ParseException, IOException, JOSEException;
}
