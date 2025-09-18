package com.innon.education.admin.jobtype.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class JobType extends CommonDTO {
	private int id;

	private String title;
	private String memo;
	private List<JobTypeDept> jobTypeDepts;
	private String delete_at;
	private int group_id;
	private Object children;
}
