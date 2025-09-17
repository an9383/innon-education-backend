package com.innon.education.auth.entity;

import lombok.Data;

@Data
public class RoleDTO {
    private int id;
    private int dept_level;
    private String description;
    private int parent_role_id;
    private String name;
    private String user_id;
    private String user_nm;
    private String dept_cd;
    private String dept_nm;

}
