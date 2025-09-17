package com.innon.education.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.innon.education.admin.manage.repository.ManagerUser;
import com.innon.education.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private int group_id;
    private String user_name;
    private String user_nm;
    private String dept_nm;
    private String dept_cd;

    private Date last_login_date;
    private String loginDateStr;
    private String plant_cd;
    private String message;
    private List<ManagerUser> manager_info;
    private List<Object> auth_menu;
    private List<String> user_role;

}