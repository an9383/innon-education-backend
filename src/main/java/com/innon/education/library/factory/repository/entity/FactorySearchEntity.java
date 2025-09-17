package com.innon.education.library.factory.repository.entity;

import com.innon.education.library.factory.repository.model.Factory;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class FactorySearchEntity {
    private String code_id;         // 문서유형
    private String document_num;    // 문서번호
    private String reg_user_id;     // 작성자
    private String reg_user_dept;   // 작성부서
    private Date reg_date;          // 작성일

    public FactorySearchEntity(@Nullable Factory factory) {
        if(factory != null) {
            this.code_id = factory.getCode_id();
            this.document_num = factory.getDocument_num();
            this.reg_user_id = factory.getReg_user_id();
            // TODO : 부서 조회 필요
            this.reg_date = factory.getReg_date();
        }
    }
}
