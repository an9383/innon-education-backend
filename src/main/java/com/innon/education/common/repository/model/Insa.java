package com.innon.education.common.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Insa extends CommonDTO {
    private List<String> dept_cds;
    private String dept_cd;
    private String password;
    private String user_id;
    private String use_flag;
    private Date last_login_date;
    private Date pw_reset_date;
    private int pw_wrong_cnt;
    private String pw_reset_flag;
    private String del_flag;
}
