package com.innon.education.admin.group.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class GroupDept extends CommonDTO {
	private int id;
	private int group_id;
	private String title;
	private String dept_cd;
	private String delete_at;
	private String dept_level;
	private int dept_order;
	private String parent_dept_cd;
	private String parent_dept_nm;
}
