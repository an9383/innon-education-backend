package com.innon.education.management.progress.repository.entity;

import com.innon.education.management.progress.repository.dto.EducationPlanUserDTO;
import com.innon.education.management.progress.repository.model.ManagementProgress;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserAttendanceEntity {
    private int id;
    private String attend_user_id;    // 출석자
    private String attend_date;                     // 출석시간
    private int plan_id;
    private int content_id;
    private String flag;
}
