package com.innon.education.admin.board.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class QnaReply extends CommonDTO {
	private int id;                 // SEQ
	private int qna_id;             // QnA 아이디
	private String content;         // 내용
	private String sys_reg_user_nm; // 작성자명
	private int parent_id;          // 부모 아이디
	private char use_flag;          // 사용여부
	private char delete_at;         // 삭제여부

	private List<QnaReply> children;
}
