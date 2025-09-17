package com.innon.education.admin.board.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class QnaDTO extends CommonDTO {
	private int id;                 // SEQ
	private String title;           // 제목
	private String content;         // 내용
	private int write_cnt;          // 조회수
	private String sys_reg_user_nm; // 작성자명
	private char use_flag;          // 사용여부
	private String use_flag_nm;     // 사용여부명
	private char delete_at;         // 삭제여부
}
