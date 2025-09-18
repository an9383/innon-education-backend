package com.innon.education.management.result.repository.model;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class ManagementResult extends CommonDTO {
    private int id;
    private String reason;
    private int plan_id;
    private String progress_type;
    private char re_edu_yn;
    private String re_edu_start_date;
    private String re_edu_end_date;
    private char use_flag;
    private List<ReEducation> reEducation;
    private String memo;

    private int sign_id;

    private LoginRequest user;
}
