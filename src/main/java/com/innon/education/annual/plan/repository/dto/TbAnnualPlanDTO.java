package com.innon.education.annual.plan.repository.dto;

import lombok.Data;

@Data
public class TbAnnualPlanDTO {
    private int annual_edu_id;
    private int id;
    private int plan_id;
    private String sys_reg_user_id;
    private char delete_at;
    private char use_flag;
}
