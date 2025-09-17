package com.innon.education.qualified.current.repository.dto;

import lombok.Data;

@Data
public class CurrentDTO {
    private int id;
    private String job_code;                // 직무코드
    private String job_type;                // 직무항목
    private String dept_code;                 // 관련부서(char형)
    private String version;                 // 버전
    private String status;                    // 상태(char형)
    private char qualified_yn;              // 적격성여부(char형)
    private String qualified_get_date;      // 적격성인증 취득일
    private String aptitude_certi_date;     // 적성인증 유효일자

    private String dept_nm;
    private String user_id;
    private String user_nm;
}
