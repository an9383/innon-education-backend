package com.innon.education.admin.jobtype.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class JobTypeDTO extends CommonDTO {
	private int id;
	private String title;
	private String memo;
	private String sys_reg_user_nm;
	private int job_type_dept_cnt;
	private String group_nm;
	private Object children;
}
