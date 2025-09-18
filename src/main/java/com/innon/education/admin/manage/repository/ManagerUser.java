package com.innon.education.admin.manage.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class ManagerUser extends CommonDTO {
	private int id;
	private int manager_id;
	private String title;
	private String manager_user_id;
	private String dept_cd;
	private String dept_nm;
	private String user_nm;
	private String delete_at;
}
