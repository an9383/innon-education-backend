package com.innon.education.management.evaluation.repository.dto;

import lombok.Data;

@Data
public class ManagementEvalDTO {
    private int id;
    private String question_type;
    private String question_title;
    private String user_answer;
    private char grade_yn;
    private String grade_str;
    private String correct_answer;
    private int answer_cnt;
    private String explanation;
}
