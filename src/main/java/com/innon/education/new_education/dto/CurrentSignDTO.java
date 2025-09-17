package com.innon.education.new_education.dto;

import lombok.Data;

@Data
public class CurrentSignDTO {
    private int plan_id;
    private int sign_id;
    private int tpsm_id;
    private String title;
    private String signform;
    private String sign_user_id;
    private String sign_category;
    private String sign_category_nm; // 결재 유형 //교육/문서
    private String sign_status_nm; //부모 결재 상태 대기 / 완료
    private String sign_type;
    private String sign_step_nm;    // 정의된 결재 단계
    private String sign_confirm_nm; //결재 대기 / 완료 여부
    private String sign_user;
    private String manager_nm;
    private String comment;
    private String sign_user_nm;
    private String sign_state_nm;
    private String convert_upd_date;

}
