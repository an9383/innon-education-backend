package com.innon.education.management.plan.repository.model;

import lombok.Data;

import java.util.Date;

@Data
public class PlanQms {
    private String id;
    private String plant_cd;
    private String process_id;
    private String process_no;
    private String work_num;
    private String work_seq;
    private String work_dtl_seq;
    private String title;
    private String edu_contents;
    private String edu_user_id;
    private String req_user_id;
    private Date plan_end_date;
    private int plan_id;

}
