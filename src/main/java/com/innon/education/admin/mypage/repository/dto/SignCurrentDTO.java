package com.innon.education.admin.mypage.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class SignCurrentDTO extends CommonDTO {
    private int id;
    private int plan_id;
    private int plan_sign_id;
    private int manager_id;
    private String manager_user_id;
    private String manager_user_nm;

    private String title;
    private String signform;
    private String signform_nm;
    private String doc_code;
    private String req_user_id;
    private String req_user_nm;
    private String req_date;

    private int sign_id;
    private String sign_type;
    private String sign_type_nm;
    private String sign_category;
    private String sign_category_nm;
    private String state;
    private String state_nm;

    private String edu_title;
    private String edu_state;
    private String edu_state_nm;

    private String doc_title;
    private String doc_state;
    private String doc_state_nm;
    private int order_num;
    private int last_manager_id;

    private String progress_type;
    private String progress_type_nm;
    private char use_flag;
}
