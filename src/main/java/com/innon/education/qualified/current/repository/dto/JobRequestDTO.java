package com.innon.education.qualified.current.repository.dto;

import com.innon.education.code.controller.dto.CommonDTO;
import com.innon.education.qualified.current.repository.model.JobRevision;
import lombok.Data;

import java.util.List;

@Data
public class JobRequestDTO extends CommonDTO {
	private int id;
	private int job_type_id;
	private String job_title;
	private int group_id;
	private String memo;
	private String version;
	private char qualified_yn;
	private int certi_month;
	private String qualified_nm;
	private String sys_reg_user_nm;

	private String status;
	private String status_nm;
	private String sign_id;

	private char qualified_status;
	private String qualified_status_nm;

	private List<JobRevision> children;
}
