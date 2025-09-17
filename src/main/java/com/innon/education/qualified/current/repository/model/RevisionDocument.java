package com.innon.education.qualified.current.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class RevisionDocument extends CommonDTO {
	private int id;
	private int revision_id;
	private int group_id;
	private int d_id;
	private String title;
	private String document_num;
	private char revision_document;
	private char delete_at;
}
