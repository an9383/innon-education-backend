package com.innon.education.common.repository.entity;

import com.innon.education.common.repository.model.Dept;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.controller.dto.CommonDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
public class DeptEntity extends CommonDTO {
    private List<String> dept_cds;
    private String dept_cd;
    private String dept_nm;
    private String dept_order;
    private String use_flag;
    private Object children;
    private String del_flag;
    private String edu_user_id;

    public DeptEntity(Dept dept) {
        if(dept != null) {
            this.dept_cds = dept.getDept_cds();
            this.dept_cd = dept.getDept_cd();
            this.dept_nm = dept.getDept_nm();
            this.dept_order = dept.getDept_order();
            this.use_flag = dept.getUse_flag();
            this.setSearch_txt(dept.getSearch_txt());
            this.setSearch_type(dept.getSearch_type());
        }
    }

    public DeptEntity(Insa insa) {
        if(insa != null) {
            this.dept_cds = insa.getDept_cds();
        }
    }
}
