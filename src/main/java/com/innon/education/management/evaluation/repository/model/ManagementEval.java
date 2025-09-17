package com.innon.education.management.evaluation.repository.model;

import lombok.Data;

@Data
public class ManagementEval {
    private int id;                 // SEQ
    private int question_info_id;            // 문제항목
    private int plan_id;
    private String qms_user_id;             // 수강생
    private String user_answer;     // 수강생 답변
    private char grade_yn;        // 정답유무(char형)
    private String status;          // 상태(char형[T: 임시저장, S:제출])
    private int answer_cnt;
    private int qb_q_id;

    private String view;
}
