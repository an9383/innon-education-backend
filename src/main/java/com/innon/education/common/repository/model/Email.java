package com.innon.education.common.repository.model;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class Email extends CommonDTO {
    private int id;
    private String sender;
    private String receiver;
    private Date send_date;
    private String success_code;
    private String otp;
    private char use_flag;
}
