package com.innon.education.educurrent.repository.model;

import lombok.Data;

@Data
public class EduCurrent {
    private int plan_id;
    private String search_start_date;
    private String search_end_date;
    private String qms_user_id;
}
