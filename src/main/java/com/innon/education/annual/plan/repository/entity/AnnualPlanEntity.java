package com.innon.education.annual.plan.repository.entity;

import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import com.innon.education.annual.plan.repository.model.AnnualPlan;
import lombok.Data;

@Data
public class  AnnualPlanEntity  {
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
    private String sys_reg_user_id;         // 작성자
    private String edu_goal;            // 교육목적
    private String edu_coverage;        // 적용범위
    private String res_role;            // 역할 및 책임
    private String progress_type;
    private int edu_year;            // 연도
    private char use_flag;
    private char delete_at;
    private String version;
    private String edu_user_id;
    private String edu_user_nm;
    private int sign_id;
    private String memo;
    private int group_id;

    private int old_id;
    private String save_yn;


    public AnnualPlanEntity(AnnualPlan annualPlan) {
        this.id = annualPlan.getId();
        this.edu_type = annualPlan.getEdu_type();
        this.dept_cd = annualPlan.getDept_cd();
        this.completion_month = annualPlan.getCompletion_month();
        this.proceed_type = annualPlan.getProceed_type();
        this.evaluation_type = annualPlan.getEvaluation_type();
        this.plan_type = annualPlan.getPlan_type();
        this.edu_status = annualPlan.getEdu_status();
        this.title = annualPlan.getTitle();
        this.edu_num = annualPlan.getEdu_num();
        this.document_num = annualPlan.getDocument_num();
        this.sys_reg_user_id = annualPlan.getSys_reg_user_id();
        this.edu_goal = annualPlan.getEdu_goal();
        this.edu_coverage = annualPlan.getEdu_coverage();
        this.res_role = annualPlan.getRes_role();
        this.progress_type = annualPlan.getProgress_type();
        this.edu_year = annualPlan.getEdu_year();
        this.use_flag = annualPlan.getUse_flag();
        this.delete_at = annualPlan.getDelete_at();
        this.version = annualPlan.getVersion();
        this.edu_user_id = annualPlan.getEdu_user_id();
        this.edu_user_nm = annualPlan.getEdu_user_nm();
        this.sign_id = annualPlan.getSign_id();
        this.memo = annualPlan.getMemo();
        this.group_id = annualPlan.getGroup_id();
        this.save_yn = annualPlan.getSave_yn();
    }

    public AnnualPlanEntity(AnnualPlanDTO annualPlan) {
        this.edu_type = annualPlan.getEdu_type();
        this.dept_cd = annualPlan.getDept_cd();
        this.title = annualPlan.getTitle();
        this.edu_goal = annualPlan.getEdu_goal();
        this.edu_coverage = annualPlan.getEdu_coverage();
        this.res_role = annualPlan.getRes_role();
        this.edu_year = annualPlan.getEdu_year();
        this.group_id = annualPlan.getGroup_id();
        this.use_flag = annualPlan.getUse_flag();
        this.delete_at = annualPlan.getDelete_at();
        this.old_id = annualPlan.getOld_id();
    }
}
