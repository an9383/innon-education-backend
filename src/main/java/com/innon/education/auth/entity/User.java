package com.innon.education.auth.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
@Data
public class User implements UserDetails  {

    private int id;

    private String user_id;

    @NotEmpty(message = "Please provide your first name.")
    private String firstName;
    @NotEmpty(message = "Please provide your last name.")
    private String lastName;

    @NotEmpty(message = "Please provide your password.")
    private String password;

    private int group_id;
    private String dept_cd;
    private String dept_nm;
    private String plant_cd;
    private String user_nm;
    private int use_flag;
    private String email;
    private int del_flag;
    private Collection<Role> roles = new ArrayList<>();
    private ArrayList<GrantedAuthority> authorities;
    private Date last_login_date;
    private Date pw_reset_date;
    private int pw_wrong_cnt;
    private String pw_reset_flag;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(authorities);
        return authList;
    }

    @Override
    public String getUsername() {
        return "";
    }
}
