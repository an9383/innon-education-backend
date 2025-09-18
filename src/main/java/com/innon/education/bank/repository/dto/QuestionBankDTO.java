package com.innon.education.bank.repository.dto;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class QuestionBankDTO extends CommonDTO {
	private int id;
	private int group_id;
	private String group_nm;
	private String edu_type;
	private String edu_type_nm;
	private String memo;
	private int question_cnt;
	private char use_flag;
	private String use_flag_nm;
	private char delete_at;
	private String sys_reg_user_nm;

}
