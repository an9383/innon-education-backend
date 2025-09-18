package com.innon.education.bank.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class QuestionBank extends CommonDTO {
	private int id;
	private int group_id;
	private String edu_type;
	private String memo;
	private char use_flag;
	private char delete_at;
	private String sys_reg_user_nm;

	private List<QbQuestion> questions;
}