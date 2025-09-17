package com.innon.education.jwt.service.impl;

import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.jwt.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Override
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(CustomUserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, CustomUserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(CustomUserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration);
    }

    @Override
    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        try {

            final String username = extractUsername(token);
            return (username.equals(userDetails.getUser_id())) && !isTokenExpired(token);
        }  catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token -> Message: {}", ex);
            throw new InvalidTokenRequestException("JWT", token, "Token이 만료됨. 갱신 필요");
        } catch (JwtException e) {
            return false;
        }
    }

    private String buildToken(Map<String, Object> extraClaims, CustomUserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUser_id())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser().verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
