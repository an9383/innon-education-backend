package com.innon.education.management.plan.repository.entity;

import com.innon.education.management.plan.repository.model.PlanQms;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class PlanQmsEntity {
    private int id;
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

    public PlanQmsEntity() {}

    public PlanQmsEntity(PlanQms plan) {
        this.plant_cd = plan.getPlant_cd();
        this.process_id = plan.getProcess_id();
        this.process_no = plan.getProcess_no();
        this.work_num = plan.getWork_num();
        this.work_seq = plan.getWork_seq();
        this.work_dtl_seq = plan.getWork_dtl_seq();
        this.title = plan.getTitle();
        this.edu_contents = plan.getEdu_contents();
        this.edu_user_id = plan.getEdu_user_id();
        this.req_user_id = plan.getReq_user_id();
        this.plan_end_date = plan.getPlan_end_date();
        this.plan_id = plan.getPlan_id();
    }
}
