package com.innon.education.library.document.repasitory.model;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.common.repository.dto.LogDTO;
import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class DocumentMemo extends CommonDTO {
	private int id;
	private int document_id;
	private String sys_reg_user_nm;
	private String memo;
	private String status;
	private String type;

	private List<PlanSignManager> signUserList;
	private Date current_start_date;
	private Date current_end_date;
	private int sign_id;
	private String sign_type;
	private int[] documentMemoIds;
	private List<LogDTO> documentLog;
	private String signform;

	public DocumentMemo() {}

	private LoginRequest user;

	public DocumentMemo(Document document, int document_id) {
		this.document_id = document_id;
		this.memo = document.getEtc();
	}
}
