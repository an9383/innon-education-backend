package com.innon.education.management.progress.repository.entity;

import com.innon.education.management.progress.repository.model.ManagementProgress;
import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ManagementProgressEntity {
    private int id;                 // SEQ
    private int plan_id;            // 교육계획
    private String qms_user_id;     // 수강생
    private String reason;          // 사유
    private String status;          // 상태(char형)[T:임시저장, S:제출]
    private String title;           // 제목
    private String work_num;        // 문서번호

    public ManagementProgressEntity(@Nullable ManagementProgress progress) {
        if(progress != null) {
            this.id = progress.getId();
            this.plan_id = progress.getPlan_id();
            this.qms_user_id = progress.getQms_user_id();
            this.status = progress.getStatus();
            this.reason = progress.getReason();
            this.title = progress.getTitle();
            this.work_num = progress.getWork_num();
        }
    }
}
