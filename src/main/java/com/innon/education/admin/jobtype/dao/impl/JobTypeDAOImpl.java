package com.innon.education.admin.jobtype.dao.impl;

import com.innon.education.admin.jobtype.dao.JobTypeDAO;
import com.innon.education.admin.jobtype.repository.JobType;
import com.innon.education.admin.jobtype.repository.JobTypeDTO;
import com.innon.education.admin.jobtype.repository.JobTypeDept;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobTypeDAOImpl implements JobTypeDAO {
    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveJobType(JobType jobType) {
        return sqlSession.insert("com.innon.education.admin.mapper.jobtype-mapper.saveJobType", jobType);
    }

    @Override
    public int saveJobTypeDept(JobTypeDept jobTypeDept) {
        return sqlSession.insert("com.innon.education.admin.mapper.jobtype-mapper.saveJobTypeDept", jobTypeDept);
    }

    @Override
    public List<JobTypeDTO> findJobTpyeList(JobType jobType) {
        return sqlSession.selectList("com.innon.education.admin.mapper.jobtype-mapper.findJobTypeList", jobType);
    }

    @Override
    public List<JobTypeDept> findJobTypeDeptList(JobType jobType) {
        return sqlSession.selectList("com.innon.education.admin.mapper.jobtype-mapper.findJobTypeDeptList", jobType);
    }

    @Override
    public int updateJobTypeUpdate(JobType jobType) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.jobtype-mapper.updateJobTypeUpdate", jobType);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateJobTypeDept(JobTypeDept jobTypeDept) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.jobtype-mapper.updateJobTypeDept", jobTypeDept);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<JobTypeDept> findJobTypeDeptByDeptCd(String dept_cd) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.jobtype-mapper.findJobTypeDeptByDeptCd", dept_cd);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
