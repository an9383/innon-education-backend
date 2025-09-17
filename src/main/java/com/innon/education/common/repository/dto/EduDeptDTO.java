package com.innon.education.common.repository.dto;

import lombok.Data;

@Data
public class EduDeptDTO {
    private String id;
    private String uid;
    private String dept_cd;
    private String dept_nm;
    private String parent_dept_cd;
    private String parent_dept_nm;
    private String nm_path;
    private String cd_path;
    private String dept_level;
    private int dept_order;
    private String use_flag;
    private String use_flag_nm;

    private String view_nm;             // 임시 조회 컬럼
    private Object children;
    private String plant_cd;
}
