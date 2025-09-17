package com.innon.education.management.result.repository.entity;

import com.innon.education.management.result.repository.model.ReEducation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReEducationEntity {
             // 재교육 여부
    private int re_edu_cnt;                 // 회차
    private int plan_id;                    // 교육계획 아이디
    private String re_user_id;
}
