package com.innon.education.common.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class Login extends CommonDTO {
    private int id;
    private String user_id;
    private Date login_date;
    private String login_type;
}
