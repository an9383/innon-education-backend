package com.innon.education.jwt.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");

        try{
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e){
            //만료 에러
            request.setAttribute("exception", e.getMessage());

        }
//        filterChain.doFilter(request, response);

    }
}