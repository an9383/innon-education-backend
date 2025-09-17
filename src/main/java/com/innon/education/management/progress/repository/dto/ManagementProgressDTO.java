package com.innon.education.management.progress.repository.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ManagementProgressDTO {
    private int id;                                     // SEQ
    private String title;                               // 교육제목
    private String status;                              // 상태
    private String edu_user_id;                         // 교육자
    private int year;                                   // 년도
    private int month;                                  // 월
    private String edu_type;                             // 교육유형
    private String plan_start_date;                       // 계획일정 시작일
    private String plan_end_date;                         // 계획일정 종료일
    private String approve_step;                         // 승인단계
    private int question_num;                           // 출제문항 수
    private String work_num;                             // 업무번호
    private String course_id;                            // 교육과정
    private String duty_category;                        // 직무항목
    private String document_id;                          // 문서번호
    private String proceed_type;                         // 진행방법
    private String evaluation_type;                      // 평가방법
    private String completion_type;                      // 완료기준
    private char progress_type;
    private String valid_date;                           // 유효기간
    private String relation_system;                      // 관련시스템
    private String relation_num;
    private int passing_rate;
    private int re_edu_cnt;
    private int parent_id;
    private Date create_at;

    private List<String> planUserList;                  // 교육대상자
    private List<String> helpUrlList;                    // 참고URL
}
