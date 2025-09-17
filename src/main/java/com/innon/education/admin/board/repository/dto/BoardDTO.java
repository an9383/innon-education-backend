package com.innon.education.admin.board.repository.dto;

import lombok.Data;

@Data
public class BoardDTO {
    private String title;               // 제목
    private String content;             // 내용
    private String board_type;          // 게시판유형(char형)
    private String use_yn;              // 사용여부(char형)
    private int reg_id;                 // 작성자
    private String reg_date;            // 등록일
    private String update_date;         // 수정일
    private String delete_date;         // 삭제일
}
