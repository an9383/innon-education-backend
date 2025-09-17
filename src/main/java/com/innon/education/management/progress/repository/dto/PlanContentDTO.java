package com.innon.education.management.progress.repository.dto;

import lombok.Data;

@Data
public class PlanContentDTO {
    private int id;
    private int plan_id;
    private String url_addr;
    private String edu_contents;
    private int seconds;
}
