package com.innon.education.management.plan.repository.model;

import lombok.Data;

@Data
public class QuestionInfo {
    private int id;                 // SEQ
    private int plan_id;            // 교육계획
    private int qb_q_id;            // 문제은행 아이디
    private int question_num;       // 문제수
    private int test_num;           // 시험횟수
    private String question_type;   // 문제유형
    private String edu_type;        // 교육유형
    private String duty_category;   // 직무항목
    private String document_id;     // 문서번호
    private char delete_at;
}
