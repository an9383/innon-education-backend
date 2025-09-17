package com.innon.education.admin.board.repository.entity;

import com.innon.education.admin.board.repository.model.Notice;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NoticeEntity {
    private int id;                     // SEQ
    private String title;               // 제목
    private String content;             // 내용
    private int order_num;              // 공지순서
    private String noti_type;              // 공지분류
    private String url_addr;            // 링크주소
    private String sys_reg_user_id;                 // 작성자

    public NoticeEntity(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.order_num = notice.getOrder_num();
        this.noti_type = notice.getNoti_type();
        this.url_addr = notice.getUrl_addr();
        this.sys_reg_user_id = notice.getSys_reg_user_id();
    }
}
