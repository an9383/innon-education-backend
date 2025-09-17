package com.innon.education.educurrent.repository.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class EduCurrentCntDTO {
    private Object result;
    private Object evaluation;
    private Object progress;
    private Object plan;
    private Object total;
}
