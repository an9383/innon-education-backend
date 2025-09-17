package com.innon.education.library.factory.repository.dto;

import lombok.Data;

@Data
public class FactoryDTO {
    private int id;                 // SEQ
    private String title;           // 제목
    private int regUserId;          // 작성자
    private int liId;               // 위치
    private String confirmYn;       // 승인상태
    private String codeId;          // 문서유형
    private String regDate;         // 작성일
    private int dcId;               // 문서상태
    private String documentNum;     // 문서번호
    private String etc;             // 비고
    private String writeYear;       // 작성년도
}
