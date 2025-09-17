package com.innon.education.common.repository.entity;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogEntity extends CommonDTO {
    private int id;
    private int table_id;
    private String page_nm;
    private String type;
    private String url_addr;
    private String state;
    private String reg_user_id;
    private String reg_user_nm;
}
