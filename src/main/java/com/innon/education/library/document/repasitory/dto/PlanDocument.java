package com.innon.education.library.document.repasitory.dto;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class PlanDocument extends CommonDTO {
	private int id;
	private int plan_id;
	private int document_id;
	private int d_id;
	private char delete_at;

}
