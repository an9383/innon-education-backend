package com.innon.education.common.repository.entity;

import lombok.Data;

@Data
public class CodeEntity {
    private int id;
    private String parent_code;
    private int order_num;
    private String main_code;
    private String short_name;
    private String code_name;
    private String discription;
    private String use_yn;
    private int depth_level;
}
