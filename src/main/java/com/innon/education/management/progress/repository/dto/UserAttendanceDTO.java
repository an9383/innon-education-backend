package com.innon.education.management.progress.repository.dto;

import lombok.Data;

@Data
public class UserAttendanceDTO {
    private int id;
    private String attend_user_id ;    // 출석자
    private String attend_date_str;                     // 출석시간
    private int plan_id;
    private int content_id;
    private String edu_contents;
    private String flag;
}
