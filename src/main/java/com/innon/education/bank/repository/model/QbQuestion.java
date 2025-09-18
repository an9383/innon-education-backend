package com.innon.education.bank.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class QbQuestion extends CommonDTO {
	private int id;
	private int qb_id;
	private String title;
	private char essential;
	private String question_type;
	private String question_type_nm;
	private String grade;
	private String grade_nm;
	private String explanation;
	private String edu_type;
	private String edu_type_nm;
	private char use_flag;
	private char delete_at;
	private String sys_reg_user_nm;
	private String use_flag_nm;

	private List<QbQItem> answers;
}