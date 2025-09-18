package com.innon.education.library.location.repository.model;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class Location extends CommonDTO {
	private String parent_code;
	private String location_name;
	private String location_code;
	private int order_num;
	private String main_code;
	private String short_name;
	private char use_flag;
	private int depth_level;
	private char delete_at;

	private String company;
	private String document;
	private String floor;
	private String rack;

	private int id;
	private String replace_location_code;

	private String discription;

	private List<Location> children;
}
