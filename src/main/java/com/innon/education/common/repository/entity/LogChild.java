package com.innon.education.common.repository.entity;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LogChild {
    private int id;
    private int log_id;

    private String field;
    private String before_value;
    private String new_value;
    private String reg_user_id;
}
