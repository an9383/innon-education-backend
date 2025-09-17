package com.innon.education.admin.mypage.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class Mypage extends CommonDTO {
    private String gate_cd;
    private String user_id;
    private String reg_date;
    private String reg_user_id;
    private String mod_date;
    private String mod_user_id;
    private String del_flag;
    private String use_flag;
    private String company_cd;
    private String user_type;
    private String work_type;
    private String user_nm;
    private String user_nm_eng;
    private String password;
    private String plant_cd;
    private String dept_cd;
    private String position_nm;
    private String duty_nm;
    private String employee_num;
    private String email;
    private String tel_num;
    private String cell_num;
    private String fax_num;
    private String lang_cd;
    private String country_cd;
    private String gmt_cd;
    private String progress_cd;
    private int pw_wrong_cnt;
    private String pw_reset_flag;
    private String pw_reset_date;
    private String last_login_date;
    private String ip_addr;
    private String super_user_flag;
    private String user_date_format_cd;
    private String user_number_format_cd;
    private String compulsion_pw_reset_flag;
    private String dept_nm;
    private String position_cd;
    private String chief_flag;

    private String sign_category;
    private String sign_category_nm;

    private String req_date_end;
    private String req_date_start;

    private String req_user_id;
    private String manager_user_id;
}
