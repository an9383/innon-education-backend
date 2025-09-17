package com.innon.education.admin.mypage.repository.dto;

import lombok.Data;

@Data
public class MyCurrentDTO {
    private int signEduCurrentCnt;
    private int signDocCurrentCnt;

    private int eduPlanCnt;
    private int eduPrgsCnt;
    private int eduEvalCnt;
    private int eduResultCnt;

    private int docTransCnt;
    private int docSubCnt;
    private int docReadCnt;
    private int docLoanCnt;
}
