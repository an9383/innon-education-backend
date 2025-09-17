package com.innon.education.bank.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class QbQItem extends CommonDTO {
	private int id;
	private int qb_q_id;
	private String item_name;
	private String correct_answer;
	private int order_num;
	private char use_flag;
	private char delete_at;
	private String sys_reg_user_nm;

}
