package com.innon.education.qualified.current.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class JobRevision extends CommonDTO {
	private int id;
	private int qualified_id;
	private int revision_id;
	private int job_type_id;
	private String purpose;
	private String reason;
	private String mandate;
	private String mandate_nm;
	private String content;
	private char use_flag;
	private char delete_at;

	private String purpose_tx;
	private String main_duty_task;
	private String education;
	private String major;
	private String career;
	private String license;
	private String capability;
	private String pq;
	private String language_tx;
	private String healthy;
	private String training;
	private String superior;
	private String co_worker;
	private String junior;
	private String deputy;
	private String internal;
	private String external_tx;

	private String sys_reg_user_nm;
	private String version;
	private String job_title;
	private char qualified_yn;
	private String qualified_nm;
	private String memo;
	private String confirm_date;
	private int certi_month;
}
