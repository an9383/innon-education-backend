package com.innon.education.auth.entity;

import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class RoleUser extends CommonDTO {
	private int id;
	private String user_id;
	private int role_id;
}
