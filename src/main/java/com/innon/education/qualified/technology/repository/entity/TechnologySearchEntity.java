package com.innon.education.qualified.technology.repository.entity;

import com.innon.education.qualified.technology.repository.model.Technology;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class TechnologySearchEntity {
    private String job_code;
    private int qualified_id;
    private char dept_code;
    private char status;
    private char qualified_yn;
    private String user_id;
    private List<Character> dept_list;

    public TechnologySearchEntity(@Nullable Technology technology) {
        if(technology != null) {
            this.job_code = technology.getJob_code();
            this.qualified_id = technology.getQualified_id();
            this.status = technology.getStatus();
            this.qualified_yn = technology.getQualified_yn();
            this.user_id = technology.getUser_id();
            this.dept_list = technology.getDept_list();
        }
    }
}
