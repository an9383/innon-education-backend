package com.innon.education.admin.manage.repository;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

import java.util.List;

@Data
public class Manager extends CommonDTO {
	private int id;

	private String title;
	private String memo;
	private List<ManagerUser> managerUsers;
	private String delete_at;
	private String manager_user_id;
	private Object children;

}
