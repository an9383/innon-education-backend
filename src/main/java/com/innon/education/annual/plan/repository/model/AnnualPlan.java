package com.innon.education.annual.plan.repository.model;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.controller.dto.CommonDTO;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import lombok.Data;

import java.util.List;

@Data
public class AnnualPlan extends CommonDTO {
	private int id;
	private String edu_type;            // 교육유형
	private String dept_cd;             // 교육대상
	private int completion_month;       // 완료기한
	private String proceed_type;          // 진행방법(char형)
	private String evaluation_type;       // 평가방법(char형)
	private String plan_type;           // 교육타입(1: 공통, 2: 부서)
	private String edu_status;          // 교육상태(char형)
	private String title;               // 교육제목
	private String edu_num;             // 교육번호
	private String document_num;        // 문서번호
	private String edu_goal;            // 교육목적
	private String edu_coverage;        // 적용범위
	private String res_role;            // 역할 및 책임
	private String progress_type;
	private int edu_year;               // 연도
	private char use_flag;
	private char delete_at;
	private String version;
	private String edu_user_id;
	private String edu_user_nm;
	private String edu_end_date;
	private int sign_id;
	private String memo;
	private LoginRequest user;
	private List<ManagementPlan> managementPlanList;
	private List<AnnualPlan> deptPlanList;
	private PlanSignManager planSignUser;

	private AnnualPlan be_annual_plan;

	private String save_yn;

}
