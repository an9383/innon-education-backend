package com.innon.education.common.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class Code extends CommonDTO {
	private int id;
	private String parent_code;
	private int order_num;
	private String main_code;
	private String short_name;
	private String code_name;
	private String discription;
	private char use_flag;
	private int depth_level;

	private String replace_code_name;
	private List<Code> children;
}
