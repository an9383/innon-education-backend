package com.innon.education.qualified.technology.repository.dto;

import lombok.Data;

@Data
public class TechnologyDTO {
    private String job_code;                // 직무코드
    private String job_type;                // 직무항목
    private char dept_code;                 // 관련부서(char형)
    private String version;                 // 버전
    private char status;                    // 상태(char형)
    private char qualified_yn;              // 적격성여부(char형)
    private String qualified_get_date;      // 적격성인증 취득일
    private String aptitude_certi_date;     // 적성인증 유효일자
    private int user_id;                    // 유저아이디
    private int qualified_id;               // 직무코드 아이디
}
