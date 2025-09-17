package com.innon.education.admin.jobtype.dao;

import com.innon.education.admin.jobtype.repository.JobType;
import com.innon.education.admin.jobtype.repository.JobTypeDTO;
import com.innon.education.admin.jobtype.repository.JobTypeDept;

import java.util.List;

public interface JobTypeDAO {
    int saveJobType(JobType jobType);

    int saveJobTypeDept(JobTypeDept jobTypeDept);

    List<JobTypeDTO> findJobTpyeList(JobType jobType);

    List<JobTypeDept> findJobTypeDeptList(JobType jobType);

    int updateJobTypeUpdate(JobType jobType);

    int updateJobTypeDept(JobTypeDept jobTypeDept);

    List<JobTypeDept> findJobTypeDeptByDeptCd(String dept_cd);
}
