package com.innon.education.code.controller.dto;

import lombok.Data;

import java.util.List;

import com.innon.education.common.util.DataLib;

@Data
public class PageDTO {
    private int page_no = 1;
    private int page_size = 10;
    private int total_cnt = 0;

    public <T extends CommonDTO> void setTotalCnt(List<T> list) {
        if(DataLib.isEmpty(list)) {
            this.total_cnt = 0;
            return;
        }
        if(list.get(0).getTotal_cnt() == 0) {
            this.total_cnt = list.size();
            return;
        }
        this.total_cnt = list.get(0).getTotal_cnt();
    }
}
