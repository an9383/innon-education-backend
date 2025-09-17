package com.innon.education.management.progress.repository.dto;

import lombok.Data;

@Data
public class EducationPlanUserDTO {
    private int id;                     // SEQ
    private int plan_id;                // 교육계획 아이디
    private String qms_user_id;         // 피교육자 아이디
    private char status;                // 교육 상태
    private int group_id;
}
