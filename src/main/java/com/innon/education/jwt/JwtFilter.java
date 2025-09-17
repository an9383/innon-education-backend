package com.innon.education.jwt;

import com.innon.education.jwt.dao.TokenRepository;
import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.jwt.service.CustomUserDetailsService;
import com.innon.education.jwt.service.JwtService;
import com.innon.education.jwt.service.impl.InvalidTokenRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String requestPath = request.getServletPath();
        final String[] NO_FILTER_URI = {
                "/api/auth/refresh-token",
                "/api/auth/login",
                "/api/auth/logout",
                "/api/auth/colma_login"
        };
        final boolean isRefresh = Arrays.asList(NO_FILTER_URI).contains(request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        final String refreshHeader = request.getHeader("RefreshToken");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);;

        String userEmail = null;

        try {

            userEmail = jwtService.extractUsername(jwtToken);
        } catch (ExpiredJwtException e) {


            if (!isRefresh) {

                filterChain.doFilter(request, response);
                return;
            }

           // throw new InvalidTokenRequestException("JWT", jwtToken, "TokenExpiredError");
        }  catch (Exception ex) {
            System.out.println(ex.getCause());
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);
            System.out.println(userDetails);
            boolean isTokenValid = (tokenRepository.findByToken(jwtToken) == null) ?false : true;

            if (jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }
        }
        if(refreshHeader != null){
            try {
                Date claim = jwtService.extractClaim(refreshHeader, Claims::getExpiration);
                if (!claim.before(new Date())) {
                    CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(userEmail);
                    response.setHeader("Access-Control-Expose-Headers", "refreshtoken");
                    response.setHeader("refreshtoken", jwtService.generateRefreshToken(userDetails));
                } else {
                    response.setStatus(401);
                    return;
                    //response.setHeader("test", claim.toString());
                }
            }catch(Exception e){
                //TODO
                response.setStatus(401);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
