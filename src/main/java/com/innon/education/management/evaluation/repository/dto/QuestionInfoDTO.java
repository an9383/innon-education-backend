package com.innon.education.management.evaluation.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuestionInfoDTO {
    private int id;
    private int plan_id;
    private String edu_type;
    private String edu_type_nm;
    private int group_id;
    private String group_nm;
    private String qms_user_id;
    private int question_info_id;
    private int qb_q_id;
    private String title;
    private String question_type;
    private String question_type_nm;
    private int answer_cnt;
    private String user_answer;
    private String status;
    private char essential;
    private List<QuestionItemDTO> question_items;
}
