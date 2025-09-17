package com.innon.education.management.plan.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class UserEduCurrentDTO extends CommonDTO {
    private String user_id;
    private int edu_cnt;
    private String user_nm;
    private String employee_num;
    private String email;
    private String cell_num;
}
