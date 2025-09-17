package com.innon.education.management.evaluation.repository.dto;

import lombok.Data;

@Data
public class QuestionItemDTO {
    private int id;
    private String item_name;
    private String correct_answer;
}
