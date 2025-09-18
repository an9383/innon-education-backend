package com.innon.education.qualified.current.repository.model;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.code.controller.dto.CommonDTO;
import com.innon.education.qualified.current.repository.entity.JobSkillItemEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JobSkill extends CommonDTO {
	int id;
	int jv_id;
	String job_content;
	String skill_user_id;
	char use_flag;
	char delete_at;
	Date sys_reg_date;
	int version;
	int job_skill_id;

	String reports_to;
	String deputy;
	String education;
	String major;
	String career;

	List<JobSkillItemEntity> jobSkillItemList;

	private int sign_id;
	private List<PlanSignManager> signUserList;
	private LoginRequest user;

}
