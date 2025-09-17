package com.innon.education.management.evaluation.repository.entity;

import com.innon.education.management.evaluation.repository.model.ManagementEval;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ManagementEvalEntity {
    private int id;                 // SEQ
    private int question_info_id;            // 문제항목
    private String qms_user_id;             // 수강생
    private String user_answer;     // 수강생 답변
    private String status;          // 상태(char형[T:임시저장, S:제출])
    private char grade_yn;          // 정답유무
    private String epu_id;
    private int answer_cnt;
    private int qb_q_id;

    public ManagementEvalEntity(ManagementEval eval) {
        this.id = eval.getId();
        this.qms_user_id = eval.getQms_user_id();
        this.question_info_id = eval.getQuestion_info_id();
        this.user_answer = eval.getUser_answer();
        this.status = eval.getStatus();
        this.grade_yn = eval.getGrade_yn();
        this.answer_cnt = eval.getAnswer_cnt();
        this.qb_q_id = eval.getQb_q_id();
    }
}
