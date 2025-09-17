package com.innon.education.management.progress.repository.model;

import com.innon.education.auth.dto.request.LoginRequest;
import lombok.Data;

@Data
public class ManagementProgress {
    private int id;                 // SEQ
    private int plan_id;            // 교육계획
    private int content_id;
    private String qms_user_id;     // 작성자
    private String reason;          // 사유
    private String status;          // 상태(char형)[T:임시저장, S:제출]
    private String title;           // 제목
    private String work_num;        // 문서번호
    private String roles = "admin";           // 권한(임시)
    private String flag;
    private String url_addr;
    private String edu_contents;
    private LoginRequest user;
}
