package com.innon.education.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String user_id;
    private String password;
    private String email;
    private String otp;
    private String path;
    private String refresh_token;
}
