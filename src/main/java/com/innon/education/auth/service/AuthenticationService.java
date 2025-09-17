package com.innon.education.auth.service;


import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.dto.request.RegisterRequest;
import com.innon.education.auth.dto.response.AuthenticationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationDto login(LoginRequest request) throws BadRequestException;

    AuthenticationDto register(RegisterRequest request);

    AuthenticationDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    AuthenticationDto colMalogin(LoginRequest request) throws IOException;
}
