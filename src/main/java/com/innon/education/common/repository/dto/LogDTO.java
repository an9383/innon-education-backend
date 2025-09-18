package com.innon.education.common.repository.dto;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class LogDTO extends CommonDTO {
    private int id;
    private int table_id;
    private String page_nm;
    private String url_addr;
    private String state;
    private String reg_user_id;
    private String reg_user_nm;

    private String field;
    private String before_value;
    private String new_value;
}
