package com.innon.education.admin.system.sign.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class PlanSignDetail extends CommonDTO {
	private int id;
	private int plan_sign_id;
	private int sign_manager_id;
	private String state;
	private String user_id;
	private String user_nm;
	private String comment;
	private String sys_reg_user_nm;
}
