package com.innon.education.qualified.current.repository.dto;

import lombok.Data;

@Data
public class JobSkillDTO {
	int id;
	String job_title;
	String job_code;
	String purpose;
	String main_duty_task;
	String education;
	String major;
	String career;
	String deputy;
	String job_reason;
	String mandate;
	String job_content;
	String plan_title;
	String user_id;
	String user_nm;
	String plan_end_date;
	String qualified_yn;
	String certi_month;
	String mandate_nm;
	String employee_num;
	String skill_memo;
	String mandate_user_nm;
	String dept_nm;
	String skill_expired_date_str;
	String plan_end_date_str;
	String sys_reg_user_id;
	char use_flag;
	char delete_at;

	String sys_reg_date_str;
	String mandate_user_id;
	String skill_user_id;
	int version;
	int plan_id;
	int jv_id;
	int group_id;

	String reports_to;

}
