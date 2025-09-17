package com.innon.education.qualified.current.repository.entity;

import lombok.Data;

import java.util.Date;

import com.innon.education.qualified.current.repository.dto.JobSkillItemDTO;
import com.innon.education.qualified.current.repository.model.JobSkillItem;

@Data
public class JobSkillItemEntity {
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

    // public JobSkillItemEntity(JobSkillItem jobSkillItem){
    //     this.id = jobSkillItem.getId();
    //     this.job_skill_id = jobSkillItem.getJob_skill_id();
    //     this.plan_id = jobSkillItem.getPlan_id();
    //     this.jv_id = jobSkillItem.getJv_id();
    //     this.delete_at = jobSkillItem.getDelete_at();
    //     this.qualified_yn = jobSkillItem.getQualified_yn();
    //     this.skill_memo = jobSkillItem.getSkill_memo();
    //     this.plan_end_date = jobSkillItem.getPlan_end_date();
    //     this.skill_expired_date = jobSkillItem.getSkill_expired_date();
    //     this.mandate_user_id = jobSkillItem.getMandate_user_id();
    //     this.sys_reg_user_id = jobSkillItem.getSys_reg_user_id();
    // }

    // public JobSkillItemEntity(JobSkillItemDTO jobSkillItemDTO){
    //     this.id = jobSkillItemDTO.getId();
    //     this.job_skill_id = jobSkillItemDTO.getJob_skill_id();
    //     this.plan_id = jobSkillItemDTO.getPlan_id();
    //     this.jv_id = jobSkillItemDTO.getJv_id();
    //     this.delete_at = jobSkillItemDTO.getDelete_at();
    //     this.qualified_yn = jobSkillItemDTO.getQualified_yn();
    //     this.skill_memo = jobSkillItemDTO.getSkill_memo();
    //     this.plan_end_date = jobSkillItemDTO.getPlan_end_date();
    //     this.skill_expired_date = jobSkillItemDTO.getSkill_expired_date();
    //     this.mandate_user_id = jobSkillItemDTO.getMandate_user_id();
    //     this.sys_reg_user_id = jobSkillItemDTO.getSys_reg_user_id();
    // }

}
