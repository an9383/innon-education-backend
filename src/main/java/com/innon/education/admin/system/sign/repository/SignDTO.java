package com.innon.education.admin.system.sign.repository;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class SignDTO extends CommonDTO {

	private int id;
	private String sign_category;
	private String sign_category_nm;
	private String sign_category_type;
	private String sign_category_type_nm;
	private String title;
	private String signform;
	private String signform_nm;
	private String sys_reg_user_nm;
	private int sign_user_cnt;
	private int group_id;
	private String group_nm;

}
