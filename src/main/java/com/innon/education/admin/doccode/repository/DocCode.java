package com.innon.education.admin.doccode.repository;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class DocCode extends CommonDTO {
	private int id;
	private String doc_code_name;
	private String doc_code;
	private int order_num;
	private String short_name;
	private String parent_code;
	private char use_flag;
	private char delete_at;
	private int depth_level;

	private String replace_doc_code;
}
