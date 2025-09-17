package com.innon.education.management.result.repository.dto;

import lombok.Data;

@Data
public class ManagementResultDTO {
    private int id;
    private int sign_id;
    private String reason;              // 교육결과
    private int plan_id;                // 교육계획 아이디
    private String re_edu_start_date;       // 재교육 시작일
    private String re_edu_end_date;         // 재교육 종료일
    private char re_edu_yn;
    private char use_flag;
}
