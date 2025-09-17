package com.innon.education.library.document.repasitory.entity;

import com.innon.education.controller.dto.CommonDTO;
import com.innon.education.library.document.repasitory.model.Document;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Date;

@Data
public class DocumentSearchEntity extends CommonDTO {
    private int id;
    private String title;           // 문서명
    private String code_id;         // 문서유형
    private String document_num;    // 문서번호
    private String reg_user_id;     // 작성자
    private Date reg_date;          // 작성일
    private int dc_id;              // 문서상태

    private String reg_user_dept;   // 작성부서
    private String status;          // 문서상태
    private String status_nm;       // 상태명

    private String req_user_id;     // 요청자
    private String req_user_nm;
    private String display_user_id; // 게시자
    private String display_date;      // 게시일
    private Date write_date;        // 작성일
    private String parent_code;     // 위치 부모 코드
    private String location_name;   // 위치명
    private String location_code;   // 위치코드
    private String plant_cd;        // 공장코드
    private String short_name;      // 위치 약어

    private String document_status; // 문서상태
    private String doc_code;

    private String display_date_start;
    private String display_date_end;
    private String write_date_start;
    private String write_date_end;

    private String signform;
    private String req_date_start;
    private String req_date_end;
    private String view_nm;

    private String expire_date_start;
    private String expire_date_end;
    private char use_flag;

    public DocumentSearchEntity() {}

    public DocumentSearchEntity(@Nullable Document document) {
        if (document != null) {
            this.id = document.getId();
            this.title = document.getTitle();
            this.code_id = document.getCode_id();
            this.document_num = document.getDocument_num();
            this.reg_user_id = document.getReg_user_id();
            this.reg_date = document.getReg_date();
            this.dc_id = document.getDc_id();

            this.reg_user_dept = document.getReg_user_dept();
            this.status = document.getStatus();
            this.status_nm = document.getStatus_nm();

            this.req_user_id = document.getReq_user_id();
            this.req_user_nm = document.getReq_user_nm();
            this.display_user_id = document.getDisplay_user_id();
            this.display_date = document.getDisplay_date();
            this.parent_code = document.getParent_code();
            this.location_name = document.getLocation_name();
            this.location_code = document.getLocation_code();
            this.plant_cd = document.getPlant_cd();
            this.short_name = document.getShort_name();
            this.document_status = document.getDocument_status();
            this.doc_code = document.getDoc_code();
            this.display_date_start = document.getDisplay_date_start();
            this.display_date_end = document.getDisplay_date_end();
            this.write_date_start = document.getWrite_date_start();
            this.write_date_end = document.getWrite_date_end();
            this.signform = document.getSignform();
            this.expire_date_start = document.getExpire_date_start();
            this.expire_date_end = document.getExpire_date_end();
            this.use_flag = document.getUse_flag();
            this.setSearch_txt(document.getSearch_txt());
            this.setSearch_type(document.getSearch_type());
            this.setGroup_id(document.getGroup_id());
            this.setGroupList(document.getGroupList());

        }
    }

}
