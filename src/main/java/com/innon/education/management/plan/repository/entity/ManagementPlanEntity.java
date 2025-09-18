package com.innon.education.management.plan.repository.entity;

import com.innon.education.code.controller.dto.CommonDTO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.model.EducationPlanLab;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ManagementPlanEntity extends CommonDTO {
    private int id = 0;                                 // SEQ
    private String title;                               // 교육제목
    private String status;                              // 상태(char형)
    private String edu_user_id;                         // 교육자
    private int year;                                   // 년도
    private int month;                                  // 월
    private String edu_type;                            // 교육유형(char형)
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
    private String progress_type;                       // 진척률 타입

    private String relation_num; //직무요구서 부모 테이블 241212
    private int job_qualified_id; //직무요구서 부모 테이블 241212
    private int job_revision_id; //직무요구서 버전 테이블 241212
    private int passing_rate;   //직무요구서 버전 테이블 241212


    private String valid_date;                          // 유효기간
    private String relation_system;                     // 관련시스템(char형)
    private int re_edu_cnt;                             // 재교육 회차
    private int parent_id;                              // 부모 아이디
    private String search_date_type;
    private String state;
    private String sign_category;
    private String qms_user_id;
    private char use_flag;
    private char delete_at;
    public ManagementPlanEntity() {}

    public ManagementPlanEntity(@Nullable ManagementPlan plan) {
        if(plan != null) {
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
            this.search_date_type = plan.getSearch_date_type();
            this.state = plan.getState();
            this.setSearch_txt(plan.getSearch_txt());
            this.setSearch_type(plan.getSearch_type());
            this.setGroupList(plan.getGroupList());
            this.setGroup_id(plan.getGroup_id());
            this.setSign_category(plan.getSign_category());
            this.setQms_user_id(plan.getQms_user_id());
            this.setUse_flag(plan.getUse_flag());
            this.setDelete_at(plan.getDelete_at());
            this.setRelation_num(plan.getRelation_num());
            this.job_qualified_id = plan.getJob_qualified_id();
            this.job_revision_id = plan.getJob_revision_id();
            this.passing_rate = plan.getPassing_rate();
            this.qms_user_id = plan.getQms_user_id();
            this.setGroupList(plan.getGroupList());
        }
    }

    public ManagementPlanEntity(int planId) {
        this.id = planId;
    }

    public ManagementPlanEntity(ManagementPlanDTO dto) {
        this.id = dto.getId();
        this.title = dto.getTitle();
        this.status = dto.getStatus();
        this.edu_user_id = dto.getEdu_user_id();
        this.year = dto.getYear();
        this.month = dto.getMonth();
        this.edu_type = dto.getEdu_type();
        this.plan_start_date = dto.getPlan_start_date();
        this.plan_end_date = dto.getPlan_end_date();
        this.approve_step = dto.getApprove_step();
        this.work_num = dto.getWork_num();
        this.course_id = dto.getCourse_id();
        this.duty_category = dto.getDuty_category();
        this.document_id = dto.getDocument_id();
        this.proceed_type = dto.getProceed_type();
        this.evaluation_type = dto.getEvaluation_type();
        this.completion_type = dto.getCompletion_type();
        this.progress_type = dto.getProgress_type();
        this.valid_date = dto.getValid_date();
        this.relation_system = dto.getRelation_system();
        this.re_edu_cnt = dto.getRe_edu_cnt();
        this.parent_id = dto.getParent_id();
        this.use_flag = dto.getUse_flag();
        this.delete_at = dto.getDelete_at();
    }

    public ManagementPlanEntity(EducationPlanLab plan) {
        this.title = plan.getTitle();
        this.plan_start_date = plan.getPlan_start_date();
        this.plan_end_date = plan.getPlan_end_date();
        this.work_num = plan.getWork_num();
        this.edu_type = plan.getEdu_type();
        this.duty_category = plan.getDuty_category();
        this.document_id = plan.getDocument_id();
        this.proceed_type = plan.getProceed_type();
        this.evaluation_type = plan.getEvaluation_type();
        this.valid_date = plan.getValid_date();
    }
}
