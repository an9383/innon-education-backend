package com.innon.education.admin.group.repository;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class Group extends CommonDTO {
	private int id;

	private String title;
	private String memo;
	private List<GroupDept> groupDepts;
	private String delete_at;
	private Object children;
}
