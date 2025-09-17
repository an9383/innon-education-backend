package com.innon.education.new_education.dto;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.common.repository.model.Dept;
import lombok.Data;

import java.util.List;

@Data
public class New_SearchDTO {

    private int plan_id;
    private int sign_id;
    private int plan_sign_id;

    private String signform;

    private String sign_category;
    private int group_id;
    private List<Group> groupList;
    private List<Dept> deptList;
    private String dept_cd;


}
