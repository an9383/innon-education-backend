package com.innon.education.auth.entity;

import com.innon.education.common.repository.entity.SystemEntity;
import lombok.Data;

import java.util.List;

@Data
public class Role  extends  SystemEntity{
    private int id;
    private int depth_level;
    private String description;
    private int parent_role_id;
    private String name;
    private String user_id;
    private String sys_reg_user_nm;
    private List<RoleUser> roleUsers;
}
