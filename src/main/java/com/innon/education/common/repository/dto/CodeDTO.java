package com.innon.education.common.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class CodeDTO extends CommonDTO {
	private int id;
	private String parent_code;
	private int order_num;
	private String plant_cd;
	private String short_name;
	private String code_name;
	private String discription;
	private char use_flag;
	private String use_flag_nm;
	private int depth_level;

	private String replace_code_name;
	private String sys_reg_user_nm;
}
