package com.innon.education.qualified.current.repository.model;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class JobSkillItem extends CommonDTO {
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

	private int sign_id;
	private List<PlanSignManager> signUserList;
	private LoginRequest user;
}
