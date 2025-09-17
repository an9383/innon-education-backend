package com.innon.education.library.document.repasitory.entity;

import com.innon.education.controller.dto.CommonDTO;
import com.innon.education.library.document.repasitory.model.Document;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Data
public class DocumentEntity extends CommonDTO {
    private String title;
    private String document_num;
    private String code_id;
    private String reg_user_id;
    private Date reg_date;
    private Date confirm_date;
    private String revised_num;
    private String write_date;
    private String write_year;
    private String write_user_id;
    private String etc;
    private String document_cnt;

    private int binder;
    private String qr_code;
    private String machineNo;
    private String test_num;
    private String doc_code;
    private String expire_date;
    private String delete_at;
    private char use_flag;
    private int id;
    private String display_date;

    public DocumentEntity() {}

    public DocumentEntity(
            Document document) {
        this.id = document.getId();
        this.title = document.getTitle();
        this.document_num = document.getDocument_num();
        this.write_user_id = document.getWrite_user_id();
        this.write_year = document.getWrite_year();
        this.write_date = document.getWrite_date();
        this.document_cnt = document.getDocument_cnt();
        this.binder = document.getBinder();

        this.code_id = document.getCode_id();
        this.confirm_date = document.getConfirm_date();
        this.revised_num = document.getRevised_num();
        this.qr_code = document.getQr_code();
        this.machineNo = document.getMachineNo();
        this.test_num = document.getTest_num();
        this.doc_code = document.getDoc_code();

        this.reg_user_id = document.getReg_user_id();
        this.expire_date = document.getExpire_date();
        this.delete_at = document.getDelete_at();
        this.setDept_cd(document.getDept_cd());
        this.setDept_nm(document.getDept_nm());
        this.setPlant_cd(document.getPlant_cd());
        this.setGroup_id(document.getGroup_id());
        this.use_flag = document.getUse_flag();

        this.display_date = document.getDisplay_date();
    }

    /*public DocumentEntity(
            String document_num,
            String title,
            String revised_num,
            Date confirm_date,
            String reg_user_id,
            String code_id,
            String write_year,
            String write_date,
            String etc,
            String document_cnt,
            String qr_code,
            String machineNo,
            String plant_cd
    ) {
        this.document_num = document_num;
        this.title = title;
        this.revised_num = revised_num;
        this.confirm_date = confirm_date;
        this.reg_user_id = reg_user_id;
        this.code_id = code_id;
        this.write_year = write_year;
        this.write_date = write_date;
        this.etc = etc;
        this.document_cnt = document_cnt;
        this.qr_code = qr_code;
        this.machineNo = machineNo;
        this.setPlant_cd(plant_cd);
    }*/
}
