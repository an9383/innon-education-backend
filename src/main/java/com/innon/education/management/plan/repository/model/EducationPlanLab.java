package com.innon.education.management.plan.repository.model;

import lombok.Data;

@Data
public class EducationPlanLab {
    private String title;
    private String plan_start_date;
    private String plan_end_date;
    private String work_num;
    private String user_dept_cd;
    private String edu_type;            // 교육유형(임시 변수명)
    private String duty_category;       // 직무항목(임시 변수명)
    private String document_id;
    private String proceed_type;        // 진행방법(임시 변수명)
    private String evaluation_type;     // 평가방법(임시 변수명)
    private String edu_contents;
    private String qualified_info;      // 적격성정보(임시 변수명)
    private String valid_date;          // 유효기간
    private String unit;                // 단위(임시 변수명)
}
