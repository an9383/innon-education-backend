package com.innon.education.admin.jobtype.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class JobTypeDept extends CommonDTO {
	private int id;
	private int job_type_id;
	private String dept_cd;
	private String delete_at;
	private String dept_level;
	private int dept_order;
	private String parent_dept_cd;
	private String parent_dept_nm;
}
