package com.innon.education.library.document.repasitory.entity;

import java.util.Date;

import com.innon.education.controller.dto.CommonDTO;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.factory.repository.model.Factory;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DocumentApplyEntity extends CommonDTO {
	private int id;
	private int document_id;        // 문서ID
	private String status;            // 문서상태
	private String req_user_id;     // 요청자
	private Date current_start_date;
	private Date current_end_date;
	private Date current_end_date_req; //대출연장요청 연장기한
	private int isSetCurrentEndDate;   //대출연장요청 연장기한 적용
	private char delete_at;
	private String update_type;

	public DocumentApplyEntity() {}

	public DocumentApplyEntity(Document document, String req_user_id) {
		this.document_id = document.getD_id();
		this.status = document.getStatus();
		// TODO : 요청자 세션 체크 아이디로 변경 필요
		this.req_user_id = req_user_id;
	}

	public DocumentApplyEntity(Factory factory) {
		this.document_id = factory.getD_id();
		this.status = factory.getStatus();
		this.current_end_date = factory.getCurrent_end_date();
	}
}
