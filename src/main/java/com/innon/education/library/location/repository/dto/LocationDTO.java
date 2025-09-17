package com.innon.education.library.location.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class LocationDTO extends CommonDTO {
	private int id; // id
	private String sys_reg_user_nm;
	private String parent_code;
	private String location_name;
	private String location_code;
	private int order_num;
	private String main_code;
	private String short_name;
	private char use_flag;
	private String use_flag_nm;
	private int depth_level;
	private char delete_at;
	private Date sys_upd_reg_date;

	private String VIEW_NM;
	private String NM_PATH;
	private String CD_PATH;

	private String use_yn_nm;
}
