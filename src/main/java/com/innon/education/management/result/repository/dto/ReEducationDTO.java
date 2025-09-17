package com.innon.education.management.result.repository.dto;

import lombok.Data;

@Data
public class ReEducationDTO {

    // 재교육 여부
    private int id;
    private int re_edu_cnt;                 // 회차
    private int plan_id;                    // 교육계획 아이디
    private String re_user_id;
}
