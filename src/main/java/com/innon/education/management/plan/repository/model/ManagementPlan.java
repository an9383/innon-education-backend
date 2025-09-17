package com.innon.education.management.plan.repository.model;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.annual.plan.repository.model.AnnualPlan;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.controller.dto.CommonDTO;
import com.innon.education.library.document.repasitory.dto.PlanDocument;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.entity.ManagementPlanUserEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ManagementPlan extends CommonDTO {
	private int id;                                     // SEQ
	private String title;                               // 교육제목
	private String status;                              // 상태(char형)
	private String edu_user_id;                         // 교육자
	private int year;                                   // 년도
	private int month;                                  // 월
	private String edu_type;                            // 교육유형(char형)
	private String plan_edu_type;						// 교육유형 -front(연간계획)
	private String plan_start_date;                     // 계획일정 시작일
	private String plan_end_date;                       // 계획일정 종료일
	private String approve_step;                        // 승인단계(char형)
	private int question_num;                           // 출제문항 수
	private String work_num;                            // 업무번호
	private String course_id;                           // 교육과정
	private String duty_category;                       // 직무항목
	private String document_id;                         // 문서번호
	private String proceed_type;                        // 진행방법(char형)
	private String evaluation_type;                     // 평가방법(char형)
	private String completion_type;                     // 완료기준(char형)
	private String progress_type;                       // 교육 진행 상태 ...
	private String relation_num;                        // 완료율
	private String valid_date;                          // 유효기간
	private String relation_system;                     // 관련시스템(char형)
	private String edu_year;
	private String memo;                                //사유
	private List<ManagementPlanUserEntity> planUserList;                   // 교육대상자
	private List<Map<String, Object>> helpUrlList;       // 참고URL
	private List<Map<String, Object>> questionInfo;           // 문제정보
	private List<Map<String, Object>> questionDetail;   // 문제정보 상세
	private List<PlanDocument> documentList;
	private String search_date_type;
	private String state;
	private List<PlanSignManager> signUserList;
	private int sign_id;
	private int re_edu_cnt;
	private int parent_id;

	private int manager_id;
	private char use_flag;
	private char delete_at;
	private String sign_category;
	private String sign_type;

	private int job_qualified_id; //직무요구서 부모 테이블 241212
	private int job_revision_id; //직무요구서 버전 테이블 241212

	private int passing_rate;
	private String qms_user_id;
	private ManagementPlan before_education;
	private LoginRequest user;
	private String filepath;
	private String website;

	private int old_id;
	private int plan_id;

	private String type;

	public ManagementPlan() {

	}

	public ManagementPlan(ManagementPlanDTO plan) {
		this.id = plan.getId();
		this.title = plan.getTitle();
		this.status = plan.getStatus();
		this.edu_user_id = plan.getEdu_user_id();
		this.year = plan.getYear();
		this.month = plan.getMonth();
		this.edu_type = plan.getEdu_type();
		this.plan_start_date = plan.getPlan_start_date();
		this.plan_end_date = plan.getPlan_end_date();
		this.approve_step = plan.getApprove_step();
		this.work_num = plan.getWork_num();
		this.course_id = plan.getCourse_id();
		this.duty_category = plan.getDuty_category();
		this.document_id = plan.getDocument_id();
		this.proceed_type = plan.getProceed_type();
		this.evaluation_type = plan.getEvaluation_type();
		this.completion_type = plan.getCompletion_type();
		this.progress_type = plan.getProgress_type();
		this.valid_date = plan.getValid_date();
		this.relation_system = plan.getRelation_system();

		this.state = plan.getState();
		this.website = plan.getWebsite();
		this.setWebsite(plan.getWebsite());
		this.setSearch_txt(plan.getSearch_txt());
		this.setSearch_type(plan.getSearch_type());
		this.setGroupList(plan.getGroupList());
		this.setGroup_id(plan.getGroup_id());
		this.setSign_category(plan.getSign_category());

		this.setUse_flag(plan.getUse_flag());
		this.setDelete_at(plan.getDelete_at());
		this.setRelation_num(plan.getRelation_num());
		this.job_qualified_id = plan.getJob_qualified_id();
		this.job_revision_id = plan.getJob_revision_id();
		this.passing_rate = plan.getPassing_rate();
		this.re_edu_cnt = plan.getRe_edu_cnt();
	}

	public ManagementPlan(AnnualPlan plan) {
		this.title = plan.getTitle();
		this.edu_type = plan.getEdu_type();
		this.setDept_cd(plan.getDept_cd());
		this.year = plan.getEdu_year();
		this.month = plan.getCompletion_month();
		this.plan_end_date = plan.getEdu_end_date();
		this.proceed_type = plan.getProceed_type();
		this.progress_type = plan.getProgress_type();
		this.evaluation_type = plan.getEvaluation_type();
		this.setEdu_user_id(plan.getEdu_user_id());
		this.use_flag = plan.getUse_flag();
	}
}