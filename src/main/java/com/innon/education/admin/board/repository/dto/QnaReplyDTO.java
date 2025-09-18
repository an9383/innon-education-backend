package com.innon.education.admin.board.repository.dto;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class QnaReplyDTO extends CommonDTO {
	private int id;                     // SEQ
	private int qna_id;                 // QnA 아이디
	private String content;             // 내용
	private int parent_id;              // 부모 아이디
	private int lev;                    // 댓글 단계
	private String id_path;             // 아이디 트리
	private String content_path;        // 댓글 트리
	private String view_content;        // 임시 보기용 컨텐츠
	private String sys_reg_user_nm;     // 작성자명
}
