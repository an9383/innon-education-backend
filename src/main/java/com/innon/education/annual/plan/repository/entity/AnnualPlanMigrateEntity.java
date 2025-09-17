package com.innon.education.annual.plan.repository.entity;

import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AnnualPlanMigrateEntity {
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
    private String reg_user_id;         // 작성자
    private String edu_goal;            // 교육목적
    private String edu_coverage;        // 적용범위
    private String res_role;            // 역할 및 책임
    private int edu_year;            // 연도
    private String status;

    public AnnualPlanMigrateEntity(AnnualPlanDTO planDto) {
        this.edu_type = planDto.getEdu_type();
        this.dept_cd = planDto.getDept_cd();
        this.completion_month = planDto.getCompletion_month();
        this.proceed_type = planDto.getProceed_type();
        this.evaluation_type = planDto.getEvaluation_type();
        this.plan_type = planDto.getPlan_type();
        this.status = planDto.getStatus();
        this.title = planDto.getTitle();
        this.edu_num = planDto.getEdu_num();
        this.document_num = planDto.getDocument_num();
        this.reg_user_id = planDto.getReg_user_id();
        this.edu_goal = planDto.getEdu_goal();
        this.edu_coverage = planDto.getEdu_coverage();
        this.res_role = planDto.getRes_role();
        this.edu_year = planDto.getEdu_year();
    }
}
