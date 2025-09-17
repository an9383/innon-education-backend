package com.innon.education.admin.board.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class NoticeDTO extends CommonDTO {
	private int id;                     // SEQ
	private String title;               // 제목
	private String content;             // 내용
	private int order_num;              // 공지순서
	private String noti_type;              // 공지분류
	private String noti_type_nm;
	private String url_addr;            // 링크주소
	private String sys_reg_user_nm;
	private char delete_at;         // 삭제일
	private char use_flag;              // 사용여부
	private String use_flag_nm;
	private String noti_start_date;
	private String noti_end_date;
}
