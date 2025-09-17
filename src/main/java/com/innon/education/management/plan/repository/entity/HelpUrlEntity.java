package com.innon.education.management.plan.repository.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Data
public class HelpUrlEntity {
    private int id;
    private int plan_id;             // 교육계획
    private String url_addr;      // url 주소
    private String edu_contents;     // 교육내용
    private int seconds;

    public HelpUrlEntity(int planId, Map<String, Object> urlMap) {
        this.plan_id = planId;
        if(urlMap.get("url_addr") != null) {
            this.url_addr = String.valueOf(urlMap.get("url_addr"));
        }
        this.edu_contents = String.valueOf(urlMap.get("edu_contents"));
        this.seconds = Integer.parseInt(String.valueOf(urlMap.get("seconds")));
    }
}
