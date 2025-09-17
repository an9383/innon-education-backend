package com.innon.education.admin.system.sign.repository;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class PlanSign extends CommonDTO {
	private int id;
	private int plan_id;
	private String status;
	private String delete_at;
	private String signform;
	private String user_nm;
	private String sign_category;
	private String sign_category_type;
	private String title;
}
