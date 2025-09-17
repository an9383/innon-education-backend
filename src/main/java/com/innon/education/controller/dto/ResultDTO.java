package com.innon.education.controller.dto;

import lombok.Data;

@Data
public class ResultDTO {

    private Boolean state;
    private Object result;
    private String message;
    private int code;
    private PageDTO page;
}
