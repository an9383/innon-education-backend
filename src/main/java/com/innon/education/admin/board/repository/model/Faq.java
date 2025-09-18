package com.innon.education.admin.board.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class Faq extends CommonDTO {
	private int id;                 // SEQ
	private String question;        // 질의
	private String answer;          // 답변
	private int order_num;          // 순서
	private String sys_reg_user_nm; // 등록자명
	private char delete_at;         // 삭제여부
	private char use_flag;          // 사용여부
	private String category;        // 카테고리
}
