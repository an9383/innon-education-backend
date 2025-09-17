package com.innon.education.jwt.dao;

import com.innon.education.jwt.dto.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {

    List<Token> findTokens(String userId);

    Token findRefreshToken(String refreshToken);

    Token findByToken(String token);

    int updateToken(Token token);

    int insertToken(Token token);


}