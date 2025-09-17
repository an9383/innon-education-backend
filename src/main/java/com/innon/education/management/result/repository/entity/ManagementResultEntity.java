package com.innon.education.management.result.repository.entity;

import com.innon.education.management.result.repository.model.ManagementResult;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ManagementResultEntity {
    private int id;
    private int sign_id;
    private String reason;              // 교육결과
    private int plan_id;                // 교육계획 아이디
    private String re_edu_start_date;       // 재교육 시작일
    private String re_edu_end_date;         // 재교육 종료일
    private char re_edu_yn;
    private char use_flag;
    public ManagementResultEntity(ManagementResult entity) {
        this.reason = entity.getReason();
        this.plan_id = entity.getPlan_id();
        this.re_edu_yn = entity.getRe_edu_yn();
        this.use_flag = entity.getUse_flag();
        this.re_edu_start_date = entity.getRe_edu_start_date();
        this.re_edu_end_date = entity.getRe_edu_end_date();
        this.id = entity.getId();

    }
}
