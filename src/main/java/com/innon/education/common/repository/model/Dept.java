package com.innon.education.common.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class Dept extends CommonDTO {
    private List<String> dept_cds;
    private String dept_cd;
    private String dept_nm;
    private String dept_order;
    private String use_flag;
    private String parent_dept_cd;
    private String parent_dept_nm;
    private String del_flag;
    private Object children;
    private String edu_user_id;
}
