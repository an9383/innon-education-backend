package com.innon.education.admin.system.sign.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class Sign extends CommonDTO {
	private int id;
	private String sign_category;
	private String sign_category_type;
	private String sign_category_type_nm;
	private String title;
	private String signform;
	private List<SignUser> signUser;
	private String delete_at;
	private int group_id;
	private char use_flag;
}
