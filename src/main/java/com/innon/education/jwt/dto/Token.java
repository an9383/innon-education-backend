package com.innon.education.jwt.dto;

import com.innon.education.auth.entity.User;
import lombok.Data;

@Data
public class Token {

    private int id;
    private String token;
    private String refresh_token;
    private TokenType token_type;
    private boolean expired;
    private boolean revoked;
    private String user_id;

    private User userDTO;


}
