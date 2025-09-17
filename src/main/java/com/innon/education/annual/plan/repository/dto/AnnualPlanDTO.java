package com.innon.education.annual.plan.repository.dto;

import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import lombok.Data;

import java.util.List;

@Data
public class AnnualPlanDTO {
    private int id;                     // SEQ
    private String edu_type;            // 교육유형
    private String dept_cd;             // 교육대상
    private String dept_nm;
    private String user_nm;
    private String sys_reg_user_id;
    private int completion_month;       // 완료기한
    private String proceed_type;          // 진행방법(char형)
    private String proceed_type_nm;       // 진행방법명(char형)
    private String evaluation_type;       // 평가방법(char형)
    private String plan_type;           // 교육타입(1: 공통, 2: 부서)
    private String plan_edu_type;            // 교육유형
    private String status;          // 교육과정
    private String status_nm;
    private String title;               // 교육제목
    private String edu_num;             // 교육번호
    private String document_num;        // 문서번호
    private String reg_user_id;         // 작성자 
    private String edu_goal;            // 교육목적
    private String edu_coverage;        // 적용범위
    private String res_role;            // 역할 및 책임
    private int edu_year;            // 연도
    private String edu_user_id;
    private String edu_user_nm;
    private String edu_type_nm;
    private String plan_edu_type_nm;
    private String progress_type_nm;
    private String evaluation_type_nm;
    private String sys_reg_user_nm;
    private String plan_sys_reg_user_nm;
    private String group_nm;
    private int group_id;
    private String month;
    private String edu_end_date;
    private String progress_type;
    private String annual_progress_type_nm;
    private String plan_sys_reg_user_id;
    private String plan_start_date;
    private String plan_end_date;
    private char use_flag;
    private String use_flag_nm;
    private char delete_at;
    private int version;
    private int plan_id;
    private int plan_sign_id;
    private int annual_sign_id;
    private  String plan_use_flag;
    private List<ManagementPlanDTO> children;
    private String save_yn;
    private int old_id;
}