package com.innon.education.management.plan.repository.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Data
public class QuestionInfoDetailEntity {
    private int questionInfo_id;            // 시험정보ID
    private String question_type;           // 문제유형
    private String edu_type;                // 교육유형
    private String duty_category;           // 직무항목
    private String document_id;             // 문서번호
    private String question_difficulty;     // 난이도
    private String question_contents;       // 문제정보
    private int order_num;                  // 순서

    public QuestionInfoDetailEntity(
            int questionInfoId,
            Map<String, Object> detailMap
    ) {
        this.questionInfo_id = questionInfoId;
        this.question_type = String.valueOf(detailMap.get("question_type"));
        this.edu_type = String.valueOf(detailMap.get("edu_type"));
        this.duty_category = String.valueOf(detailMap.get("duty_category"));
        this.document_id = String.valueOf(detailMap.get("document_id"));
        this.question_difficulty = String.valueOf(detailMap.get("question_difficulty"));
        this.question_contents = String.valueOf(detailMap.get("question_contents"));
        this.order_num = Integer.parseInt(String.valueOf(detailMap.get("order_num")));
    }
}
