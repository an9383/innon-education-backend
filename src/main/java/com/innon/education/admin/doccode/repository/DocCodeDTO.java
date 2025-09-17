package com.innon.education.admin.doccode.repository;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class DocCodeDTO extends CommonDTO {
	private int id;
	private String doc_code_name;
	private String doc_code;
	private String short_name;
	private String parent_code;
	private int depth_level;
	private int order_num;
	private char use_flag;
	private String use_flag_nm;
	private String sys_reg_user_nm;

	private String view_nm;
	private String nm_path;
	private String cd_path;
	private String short_path;
}
