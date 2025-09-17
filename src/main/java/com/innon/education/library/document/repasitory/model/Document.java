package com.innon.education.library.document.repasitory.model;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;

import java.util.Date;

@Data
public class Document extends CommonDTO {
    private int id;                 // SEQ
    private String title;           // 문서명
    private String reg_user_id;     // 작성자
    private int li_id;              // 위치
    private char confirm_yn;        // 승인상태
    private String code_id;         // 문서유형
    private Date reg_date;          // 작성일
    private int dc_id;              // 문서상태
    private String document_num;    // 문서번호
    private String etc;             // 비고
    private String write_year;      // 작성년도
    private String write_date;        // 문서 작성일
    private String write_user_id;   // 글쓴이
    private Date confirm_date;      // 승인일
    private String revised_num;     // 개정번호

    private String reg_user_dept;   // 작성부서
    private String status;          // 문서상태
    private String document_cnt;    // 권수/총권수
    private int d_id;               // 문서 아이디
    private String status_nm;       // 상태명

    private String req_user_id;     // 요청자
    private String req_user_nm;
    private String display_user_id; // 게시자
    private String display_date;      // 게시일
    private String parent_code;     // 위치 부모 코드
    private String location_name;   // 위치명
    private String location_code;   // 위치코드
    private String plant_cd;        // 공장코드
    private String short_name;      // 위치 약어

    private int binder;             // 바인더 CM

    private String document_status;
    private String memo;
    private String qr_code;
    private String machineNo;
    private String test_num;
    private String doc_code;
    private String expire_date;
    private String signform;

    private String display_date_start;
    private String display_date_end;
    private String write_date_start;
    private String write_date_end;

    private Date current_start_date;
    private Date current_end_date;

    private String expire_date_start;
    private String expire_date_end;

    private Document before_document;
    private String delete_at;
    private char use_flag;

    private LoginRequest user;

}
