package com.innon.education.library.document.repasitory.entity;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class DocumentLoanEntity extends CommonDTO {
	private int d_id;
	private String status;
	private String req_user_id;

	public DocumentLoanEntity() {}

	public DocumentLoanEntity(int d_id, String status) {
		this.d_id = d_id;
		this.status = status;
	}
}
