package com.innon.education.admin.system.sign.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class SignUser extends CommonDTO {
	private int id;
	private int sign_id;
	private String user_id;
	private String sign_type;
	private String discription;
	private String dept_cd;
	private String title;
	private String dept_nm;
	private String user_nm;

	private String delete_at;

	private int manager_id;
	private String manager_nm;

}
