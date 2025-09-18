package com.innon.education.admin.board.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class Notice extends CommonDTO {
	private int id;                     // SEQ
	private String title;               // 제목
	private String content;             // 내용
	private int order_num;              // 공지순서
	private String noti_type;           // 공지분류
	private String url_addr;            // 링크주소
	private String sys_reg_user_nm;     // 작성자명
	private char delete_at;             // 삭제일
	private char use_flag;              // 사용여부
	private Date noti_start_date;       // 공지 시작일
	private Date noti_end_date;         // 공지 종료일
	private String noti_date_start;
	private String noti_date_end;
}
