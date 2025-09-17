package com.innon.education.management.evaluation.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class EduQuestionDTO {
    private String title;
    private String plan_start_date;
    private String plan_end_date;
    private String work_num;
    private String pass_yn;
    List<QuestionInfoDTO> questions;
}
