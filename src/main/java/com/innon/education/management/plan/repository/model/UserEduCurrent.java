package com.innon.education.management.plan.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class UserEduCurrent extends CommonDTO {
    private String user_id;
    private int edu_cnt;
    private String user_nm;
    private String employee_num;
    private String email;
    private String cell_num;

    private String search_date_type;
    private String plan_start_date;                     // 계획일정 시작일
    private String plan_end_date;                       // 계획일정 종료일
}
