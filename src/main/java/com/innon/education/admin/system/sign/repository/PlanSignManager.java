package com.innon.education.admin.system.sign.repository;

import com.innon.education.annual.plan.repository.model.AnnualPlan;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.common.repository.model.Dept;
import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PlanSignManager extends CommonDTO {
	private int id;
	private String user_id;
	private String state;
	private int sign_id;
	private int plan_sign_id;
	private String sys_reg_user_nm;
	private String dept_cd;
	private String comment;
	private int order_num;
	private String flag;
	private int plan_id;
	private String sign_type;
	private LoginRequest user;

	private String plan_title;
	private String sign_title;
	private String signform;
	private String state_nm;
	private String user_nm;
	private String sign_type_nm;

	private String code_name;

	private String edu_state;
	private String doc_state;

	private String sign_category;
	private String sign_category_nm;

	private String sign_category_type;

	private int manager_id;
	private int group_id;
	private String manager_user_id;
	private String manager_nm;
	private String sign_user_nm;
	private List<Dept> deptList;

	private String status;
	private Date current_start_date;
	private Date current_end_date;
	private String memo;
	private String req_user_id;
	private List<AnnualPlan> deptPlanList;

}
