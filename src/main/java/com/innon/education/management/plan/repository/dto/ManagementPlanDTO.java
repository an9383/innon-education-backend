package com.innon.education.management.plan.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ManagementPlanDTO extends CommonDTO {

	private int id;                                     // SEQ
	private String title;                               // 교육제목
	private String status;                              // 상태
	private String edu_user_id;                         // 교육자
	private int year;                                   // 년도
	private int month;                                  // 월
	private String edu_type;                            // 교육유형
	private String plan_start_date;                     // 계획일정 시작일
	private String plan_end_date;                       // 계획일정 종료일
	private String approve_step;                        // 승인단계
	private int question_num;                           // 출제문항 수
	private String work_num;                            // 업무번호
	private String course_id;                           // 교육과정
	private String duty_category;                       // 직무항목
	private String document_id;                         // 문서번호
	private String proceed_type;                        // 진행방법
	private String evaluation_type;                     // 평가방법
	private String completion_type;                     // 완료기준
	private String completion_type_nm;                     // 완료기준
	private String progress_type;                       // 진행상태
	private String progress_type_nm;                       // 진행상태 타입
	private String valid_date;                          // 유효기간
	private String relation_system;                     // 관련시스템
	private String relation_num;
	private List<ManagementPlanUserDTO> planUserList;                   // 교육대상자
	private List<Map<String, Object>> helpUrlList;       // 참고URL
	private List<Map<String, Object>> questionInfo;           // 문제정보
	private List<Map<String, Object>> questionDetail;   // 문제정보 상세

	private int re_edu_cnt;                             // 재교육 회차
	private int parent_id;                              // 부모아이디

	private String edu_status;                          // 교육진행상태
	private String edu_user_nm;
	private String plan_user_cnt;                          // 교육대상 인원수
	private String edu_type_nm;                         // 교육유형명
	private String proceed_type_nm;                     // 진행방법명
	private String evaluation_type_nm;                  // 평가방법명
	private String relation_system_nm;                  // 적격성정보명

	private String qms_user_nms;
	private String status_nm;
	private char use_flag;
	private char delete_at;
	private String re_edu_yn;
	private String re_edu_str;

    private String state;
	private String job_code;
    private String state_nm;
    private String sys_reg_user_id;
    private int ob_qualified_id;
    private int passing_rate;                       //합격률
    private int job_qualified_id;
	private String job_qualified_nm;
    private int job_revision_id;
    private String sys_reg_date_str;
    private String filepath;
    private String filename;
    private String website;

    private String user_status;
    private int answer_cnt;
    private char pass_yn;

	private String report_reason;
	private char btn_visible;
}
