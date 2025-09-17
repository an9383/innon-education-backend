package com.innon.education.qualified.current.repository.dto;

import lombok.Data;

import java.util.Date;

@Data
public class JobSkillItemDTO {
    int id;
    int job_skill_id;
    int plan_id;
    int jv_id;
    char delete_at;
    int qualified_yn;
    String skill_memo;
    Date plan_end_date;
    Date skill_expired_date;
    String mandate_user_id;
    String sys_reg_user_id;
}
