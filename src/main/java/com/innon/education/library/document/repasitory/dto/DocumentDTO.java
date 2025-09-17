package com.innon.education.library.document.repasitory.dto;

import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class DocumentDTO extends CommonDTO {
    private int id;
    private String code_id;         // 문서유형
    private String document_num;    // 문서번호
    private String title;           // 문서명
    private String reg_user_id;     // 등록자
    private String write_year;      // 작성년도
    private String write_date;      // 작성일
    private String write_user_id;   // 작성자
    private String write_user_nm;   // 작성자명
    private String document_cnt;    // 권수/총권수
    private String qr_code;         // QR 코드
    private String status;          // 요청상태
    private String status_nm;       // 상태명
    private int row_no;             // ROWNUM

    private String req_user_id;     // 요청자
    private String display_user_id; // 게시자
    private Date display_date;      // 게시일
    private String parent_code;     // 위치 부모 코드
    private String location_name;   // 위치명
    private String location_code;   // 위치코드
    private String plant_cd;        // 공장코드
    private String short_name;      // 위치 약어

    private int binder;             // 바인더 CM

    private String confirm_date;    // 승인일자

    private String convert_display_date;

    private String current_start_date;
    private String current_end_date;
    private String current_diff_date;

    private String req_user_nm;
    private int d_id;
    private String machineNo;
    private String doc_code;
    private String expire_date;

    private String signform;
    private String signform_nm;
    private int plan_sign_id;
    private String req_date;

    private char use_flag;
    private String use_flag_nm;
}
