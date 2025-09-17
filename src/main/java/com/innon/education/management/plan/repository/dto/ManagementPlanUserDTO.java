package com.innon.education.management.plan.repository.dto;

import com.innon.education.admin.group.repository.Group;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ManagementPlanUserDTO {
    private int id;
    private int plan_id;
    private String qms_user_id;
    private String qms_user_nm;
    private String edu_status;
    private String user_id;
    private String dept_cd;
    private String dept_nm;
    private String user_nm;
    private int group_id;
    private List<Group> groupList;
}
