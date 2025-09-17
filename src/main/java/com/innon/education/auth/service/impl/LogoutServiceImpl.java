package com.innon.education.auth.service.impl;

import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.model.Login;
import com.innon.education.jwt.dao.TokenRepository;
import com.innon.education.jwt.dto.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Autowired
    CommonDAO commonDAO;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) return;

        jwt = authHeader.substring(7);

        Token storedToken = tokenRepository.findByToken(jwt);

        if (storedToken != null) {
            Login login = new Login();
            login.setUser_id(storedToken.getUser_id());
            login.setLogin_type("logout");
            commonDAO.saveLoginInfo(login);

            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.updateToken(storedToken);
        }
    }
}
