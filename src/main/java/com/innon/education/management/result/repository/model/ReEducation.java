package com.innon.education.management.result.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class ReEducation {
    private int id;
    private int plan_id;
    private int re_edu_cnt;
    private String re_user_id;
    private String qms_user_id;
}
