package com.innon.education.annual.plan.repository.entity;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;


@Data
public class AnnualPlanSearchEntity extends CommonDTO {
    private List<String> dept_list;     // 교육대상 리스트
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
    private int edu_year;            // 연도
    private String edu_user_id;         // 교육자명
    private String edu_user_nm;
    private char use_flag;
    private char delete_at;
    private int id;
    private int version;

}
