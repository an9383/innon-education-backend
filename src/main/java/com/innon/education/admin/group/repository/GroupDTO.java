package com.innon.education.admin.group.repository;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class GroupDTO extends CommonDTO {
	private int id;
	private String title;
	private String memo;
	private String sys_reg_user_nm;
	private int group_dept_cnt;
	private Object children;
}
