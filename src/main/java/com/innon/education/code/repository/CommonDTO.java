package com.innon.education.code.controller.dto;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommonDTO {
	private String convert_reg_date;

	private Date sys_reg_date;		//등록일시
	private Date sys_upd_reg_date;	//수정일시
	private String sys_reg_user_id;	//등록자ID
	private String sys_upd_user_id;	//수정자ID

	private PageDTO page;
	private int total_cnt;
	private int row_no;

	private String dept_cd;
	private String dept_nm;
	private String plant_cd;
	private int group_id;
	private String group_nm;
	private String sign_category;

	private String plant_nm;
	private String role_name;

	private String search_txt;
	private String search_type;
	//todo 정주원 group_id가 없을경우 choose otherwise에 전체 그룹 필요 ..
	private List<Group> groupList;
	private List<PlanSignManager> signUserList;

	public CommonDTO() {}

	public CommonDTO(String dept_cd, String dept_nm) {
		this.dept_cd = dept_cd;
		this.dept_nm = dept_nm;
	}
}
