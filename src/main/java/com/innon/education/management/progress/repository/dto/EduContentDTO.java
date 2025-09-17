package com.innon.education.management.progress.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class EduContentDTO {
    private String title;
    private List<PlanContentDTO> planContents;
}