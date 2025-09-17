package com.innon.education.qualified.technology.repository.model;

import lombok.Data;

import java.util.List;

@Data
public class Technology {
    private String job_code;            // 직무코드
    private int qualified_id;           // 직무코드ID
    private char dept_code;             // 관련부서(char형)
    private char status;                // 상태(char형)
    private char qualified_yn;          // 적격성여부(char형)
    private String user_id;             // 유저 ID
    
    private List<Character> dept_list;  // 관련부서 리스트
}
