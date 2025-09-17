package com.innon.education.management.plan.repository.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WorkManagementEntity {
    private int req_id;
    private String status;
    private int work_seq;
    private String req_user_id;

    public WorkManagementEntity(
            int req_id,
            String status,
            String req_user_id
    ) {
        this.req_id = req_id;
        this.status = status;
        this.req_user_id = req_user_id;
    }
}
