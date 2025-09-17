package com.innon.education.educurrent.repository.dto;

import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import lombok.Data;

import java.util.List;

@Data
public class EduCurrentListDTO {
    private EduCurrentCntDTO currentCnt;
    private List<ManagementPlanDTO> planList;
    private List<ManagementPlanDTO> progressList;
    private List<ManagementPlanDTO> evaluationList;
    private List<ManagementPlanDTO> resultList;
}
