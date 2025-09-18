package com.innon.education.qualified.current.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class RevisionContent extends CommonDTO {
	private int id;
	private int qualified_id;
	private int revision_id;
	private int group_id;
	private String content;
	private String edu_contents;
	private char delete_at;
}
