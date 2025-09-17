package com.innon.education.qualified.technology.repository.entity;

import com.innon.education.qualified.technology.repository.model.Technology;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TechnologyEntity {
    private String job_code;            // 직무코드
    private int qualified_id;           // 직무코드ID
    private char dept_code;             // 관련부서(char형)
    private char status;                // 상태(char형)
    private char qualified_yn;          // 적격성여부(char형)
    private String user_id;             // 유저 ID

    public TechnologyEntity(Technology entity) {
        this.job_code = entity.getJob_code();
        this.qualified_id = entity.getQualified_id();
        this.dept_code = entity.getDept_code();
        this.status = entity.getStatus();
        this.qualified_yn = entity.getQualified_yn();
        this.user_id = entity.getUser_id();
    }
}
