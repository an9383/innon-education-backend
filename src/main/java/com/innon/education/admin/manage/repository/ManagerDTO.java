package com.innon.education.admin.manage.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class ManagerDTO extends CommonDTO {
	private int id;
	private String title;
	private String memo;
	private String sys_reg_user_nm;
	private int manager_user_cnt;
	private Object children;
}
