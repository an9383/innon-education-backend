package com.innon.education.jwt.service;

import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.jwt.dto.Token;
import io.jsonwebtoken.Claims;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String jwtToken);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(CustomUserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, CustomUserDetails userDetails);

    String generateRefreshToken(CustomUserDetails userDetails);

    boolean isTokenValid(String token, CustomUserDetails userDetails);

}
