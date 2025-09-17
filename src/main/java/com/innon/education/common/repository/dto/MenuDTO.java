package com.innon.education.common.repository.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

@Data
public class MenuDTO extends CommonDTO {
    private int id;
    private String role_menu_id;
    private String role_id;
    private String role_menu;
    private String sys_req_user_id;
    private String up_role_id;
    private int depth_level;
}
