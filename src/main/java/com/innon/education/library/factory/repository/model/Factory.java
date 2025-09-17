package com.innon.education.library.factory.repository.model;

import lombok.Data;

import java.util.Date;

@Data
public class Factory {
    private int id;                     // SEQ
    
    private String title;               // 제목
    private String status;                // 문서상태
    private String code_id;             // 문서유형
    private String document_num;        // 문서번호
    private String reg_user_id;         // 작성자
    private Date reg_date;              // 작성일

    private int document_id;            // 문서아이디
    private int location_id;            // 위치아이디
    private String display_user_id;     // 입고 처리자
    private String display_date;          // 입고일
    private char delete_at;             // 삭제여부

    private int d_id;
    private Date current_end_date;

    private int sign_id;
}
