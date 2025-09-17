package com.innon.education.qualified.current.repository.entity;

import com.innon.education.qualified.current.repository.dto.JobSkillDTO;
import com.innon.education.qualified.current.repository.model.JobSkill;
import lombok.Data;

import java.util.Date;

@Data
public class JobSkillEntity {
    int id;
    String job_content;
    String skill_user_id;
    char use_flag;
    char delete_at;
    Date sys_reg_date;
    String sys_reg_user_id;
    int version;
    int group_id;
    String reports_to;
    String deputy;
    String education;
    String major;
    String career;

    public JobSkillEntity(JobSkill jobSkill){
        this.job_content = jobSkill.getJob_content();
        this.skill_user_id = jobSkill.getSkill_user_id();
        this.use_flag = jobSkill.getUse_flag();
        this.delete_at = jobSkill.getDelete_at();
        this.group_id = jobSkill.getGroup_id();
        this.id = jobSkill.getId();
        this.version = jobSkill.getVersion();
        this.reports_to = jobSkill.getReports_to();
        this.deputy = jobSkill.getDeputy();
        this.education = jobSkill.getEducation();
        this.major = jobSkill.getMajor();
        this.career = jobSkill.getCareer();
    }

    public JobSkillEntity(JobSkillDTO jobSkill){
        this.job_content = jobSkill.getJob_content();
        this.skill_user_id = jobSkill.getSkill_user_id();
        this.use_flag = jobSkill.getUse_flag();
        this.delete_at = jobSkill.getDelete_at();
        this.group_id = jobSkill.getGroup_id();
        this.id = jobSkill.getId();
        this.version = jobSkill.getVersion();
    }
}
