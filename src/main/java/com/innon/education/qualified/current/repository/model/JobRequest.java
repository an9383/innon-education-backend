package com.innon.education.qualified.current.repository.model;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class JobRequest extends CommonDTO {
	private int id;
	private int job_type_id;
	private int group_id;
	private String job_title;
	private String memo;
	private String version;
	private char qualified_yn;
	private int qualified_id;
	private char use_flag;
	private int certi_month;

	private char status;        // (S: 저장, T: 임시저장)

	private int sign_id;
	private List<PlanSignManager> signUserList;

	private LoginRequest user;

	private JobRevision revision;
	private List<RevisionContent> revision_contents;
	private List<RevisionDocument> revision_documents;
}
