package com.innon.education.admin.mypage.repository.entity;

import com.innon.education.admin.mypage.repository.model.Mypage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MypageEntity {
    private String gate_cd;
    private String user_id;
    private String reg_date;
    private String reg_user_id;
    private String user_type;
    private String work_type;
    private String password;
    private String duty_nm;
    private String fax_num;
    private String lang_cd;
    private String country_cd;
    private String gmt_cd;
    private String progress_cd;
    private int pw_wrong_cnt;
    private String pw_reset_flag;
    private String pw_reset_date;
    private String last_login_date;
    private String ip_addr;
    private String super_user_flag;
    private String user_date_format_cd;
    private String user_number_format_cd;
    private String compulsion_pw_reset_flag;
    private String chief_flag;

    public MypageEntity(Mypage mypage) {
        this.gate_cd = mypage.getGate_cd();
        this.user_id = mypage.getUser_id();
        this.reg_date = mypage.getReg_date();
        this.reg_user_id = mypage.getReg_user_id();
        this.user_type = mypage.getUser_type();
        this.work_type = mypage.getWork_type();
        this.password = mypage.getPassword();
        this.duty_nm = mypage.getDuty_nm();
        this.fax_num = mypage.getFax_num();
        this.lang_cd = mypage.getLang_cd();
        this.country_cd = mypage.getCountry_cd();
        this.gmt_cd = mypage.getGmt_cd();
        this.progress_cd = mypage.getProgress_cd();
        this.pw_wrong_cnt = mypage.getPw_wrong_cnt();
        this.pw_reset_flag = mypage.getPw_reset_flag();
        this.pw_reset_date = mypage.getPw_reset_date();
        this.last_login_date = mypage.getLast_login_date();
        this.ip_addr = mypage.getIp_addr();
        this.super_user_flag = mypage.getSuper_user_flag();
        this.user_date_format_cd = mypage.getUser_date_format_cd();
        this.user_number_format_cd = mypage.getUser_number_format_cd();
        this.compulsion_pw_reset_flag = mypage.getCompulsion_pw_reset_flag();
        this.chief_flag = mypage.getChief_flag();
    }
}
