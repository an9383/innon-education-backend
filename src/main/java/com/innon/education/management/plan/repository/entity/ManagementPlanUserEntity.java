package com.innon.education.management.plan.repository.entity;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ManagementPlanUserEntity extends CommonDTO {
    private int plan_user_id;
    private int plan_id;        // 교육계획
    private String qms_user_id;    // 피교육자
    private String user_id;
    private String status;
    private char delete_at;
    private char pass_yn;
    private char btn_visible;
}
