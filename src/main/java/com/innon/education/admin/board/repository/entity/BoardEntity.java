package com.innon.education.admin.board.repository.entity;

import com.innon.education.admin.board.repository.model.Board;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BoardEntity {
    private String title;               // 제목
    private String content;             // 내용
    private String board_type;          // 게시판유형(char형)
    private int reg_id;                 // 작성자

    private int board_id;               // 게시글 아이디

    public BoardEntity(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.board_type = board.getBoard_type();
        this.reg_id = board.getReg_id();
        this.board_id = board.getId();
    }
}
