package com.innon.education.common.repository.entity;

import com.innon.education.admin.group.repository.GroupDept;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.code.controller.dto.CommonDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class InsaEntity extends CommonDTO {
    private List<String> dept_cds;
    private String dept_cd;
    private String dept_nm;
    private String use_flag;
    private String del_flag;
    private String edu_user_id;

    public InsaEntity() {}

    public InsaEntity(Insa insa) {
        if(insa != null) {
            this.dept_cds = insa.getDept_cds();
            this.dept_cd = insa.getDept_cd();
            this.setSearch_txt(insa.getSearch_txt());
            this.setSearch_type(insa.getSearch_type());
            this.setPlant_cd(insa.getPlant_cd());
        }
    }

    /*public InsaEntity(List<EduDeptDTO> childEduDeptCdList) {
        List<String> deptCdList = new ArrayList<>();
        for(EduDeptDTO eduDeptDTO : childEduDeptCdList) {
            deptCdList.add(eduDeptDTO.getDept_cd());
        }
        this.dept_cds = deptCdList;
    }*/

    public InsaEntity(List<GroupDept> groupDeptList) {
        List<String> deptCdList = new ArrayList<>();
        for(GroupDept groupDept : groupDeptList) {
            deptCdList.add(groupDept.getDept_cd());
        }
        this.dept_cds = deptCdList;
    }
}
