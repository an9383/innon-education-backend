package com.innon.education.annual.plan.repository.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AnnualPlanJoinTable {

    private int id;
    private int annual_edu_id;
    private int plan_id;
    private String sys_reg_user_id;
    private Date sys_reg_date;
    private char delete_at;
    private char use_flag;

}
