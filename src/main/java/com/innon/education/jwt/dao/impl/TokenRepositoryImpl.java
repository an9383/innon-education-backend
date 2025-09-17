package com.innon.education.jwt.dao.impl;

import com.innon.education.jwt.dao.TokenRepository;
import com.innon.education.jwt.dto.Token;
import com.innon.education.jwt.dto.TokenType;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {
    @Autowired
    SqlSessionTemplate sqlsession;

    @Override
    public List<Token> findTokens(String userId) {

        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);
        token.setToken_type(TokenType.BEARER);
        token.setUser_id(userId);
        List<Token> tokenList = sqlsession.selectList("com.innon.education.auth.mapper.TokenMapper.findTokens",token);


        return tokenList;
    }

    @Override
    public Token findRefreshToken(String refreshToken) {
        Token paramToken = new Token();
        paramToken.setRefresh_token(refreshToken);
        return sqlsession.selectOne("com.innon.education.auth.mapper.TokenMapper.findTokens",paramToken);
    }

    @Override
    public Token findByToken(String token) {
        Token paramToken = new Token();
        paramToken.setRevoked(false);
        paramToken.setExpired(false);
        paramToken.setToken(token);
        return sqlsession.selectOne("com.innon.education.auth.mapper.TokenMapper.findTokens",paramToken);
    }

    @Override
    public int updateToken(Token token) {
        int saveId = 0;
        try {
            saveId = sqlsession.update("com.innon.education.auth.mapper.TokenMapper.updateToken",token);

        }catch (Exception e) {
            System.out.println(e.getCause());
        }


        return saveId;
    }

    @Override
    public int insertToken(Token token) {
        int saveId = 0;
        try {
             saveId = sqlsession.insert("com.innon.education.auth.mapper.TokenMapper.insertToken",token);

        }catch (Exception e) {
            System.out.println(e.getCause());
        }


        return saveId;
    }
}
