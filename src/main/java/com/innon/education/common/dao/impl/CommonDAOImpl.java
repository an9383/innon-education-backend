package com.innon.education.common.dao.impl;

import com.innon.education.admin.group.repository.Group;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.dto.EduDeptDTO;
import com.innon.education.common.repository.dto.EduInsaDTO;
import com.innon.education.common.repository.dto.LogDTO;
import com.innon.education.common.repository.dto.MenuDTO;
import com.innon.education.common.repository.entity.DeptEntity;
import com.innon.education.common.repository.entity.InsaEntity;
import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Dept;
import com.innon.education.common.repository.model.Email;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.common.repository.model.Login;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CommonDAOImpl implements CommonDAO {

    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public List<EduDeptDTO> findEduDeptList(DeptEntity deptEntity) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findEduDeptList", deptEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<EduDeptDTO> findChildEduDeptCdList(DeptEntity deptEntity) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findChildEduDeptCdList", deptEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<EduInsaDTO> findEduInsaList(InsaEntity insaEntity) {
        try {
//            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findEduInsaList", insaEntity);
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findEduInsaList_page", insaEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<EduInsaDTO> findEduPopupInsaList(Insa insa) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findEduPopupInsaList", insa);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findEduInsaListCnt(InsaEntity insaEntity) {
        try {
            return sqlSession.selectOne("com.innon.education.common.mapper.common-mapper.findEduInsaListCnt", insaEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public String findFactoryCdByUserId(String user_id) {
        try {
            return sqlSession.selectOne("com.innon.education.common.mapper.common-mapper.findFactoryCdByUserId", user_id);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<EduInsaDTO> findEduInsaListCustom(String depts) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findEduInsaListCustom", depts);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public String findAllDepth() {
        try {
            return sqlSession.selectOne("com.innon.education.common.mapper.common-mapper.findAllDepth");
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveLog(LogEntity logEntity) {
        try {
            return sqlSession.insert("com.innon.education.common.mapper.common-mapper.saveLog", logEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveLogDetail(LogChild logChild) {
        try {
            return sqlSession.insert("com.innon.education.common.mapper.common-mapper.saveLogDetail", logChild);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<LogDTO> findLogList(LogEntity logEntity) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findLogList", logEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<LogChild> logChildList(LogEntity logEntity) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findLogChildList", logEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateInsa(Insa insa) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.common-mapper.updateInsa", insa);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateDept(Dept dept) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.common-mapper.updateDept", dept);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<EduDeptDTO> findEduDeptPopupList(DeptEntity deptEntity) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findEduDeptPopupList", deptEntity);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<Group> findGroupInfoByDeptCd(String dept_cd) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findGroupInfoByDeptCd", dept_cd);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<Dept> findTopDeptByDeptCd(String dept_cd) {
        try {
            return sqlSession.selectList("com.innon.education.common.mapper.common-mapper.findTopDeptByDeptCd", dept_cd);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveLoginInfo(Login login) {
        try {
            return sqlSession.insert("com.innon.education.common.mapper.common-mapper.saveLoginInfo", login);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public Email findEmailOtp(Email email) {
        try {
            return sqlSession.selectOne("com.innon.education.common.mapper.common-mapper.findEmailOtp", email);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveEmailOtp(Email email) {
        try {
            return sqlSession.insert("com.innon.education.common.mapper.common-mapper.saveEmailOtp", email);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateEmailOtpFlag(Email email) {
        try {
            return sqlSession.update("com.innon.education.common.mapper.common-mapper.updateEmailOtpFlag", email);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<MenuDTO> findRoleMenuList(MenuDTO menuDto) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.manager-mapper.findRoleMenuList", menuDto);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteRoleMenuList(String roleId) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.manager-mapper.deleteRoleMenuList", roleId);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveRoleMenu(MenuDTO menuDto) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.manager-mapper.saveRoleMenu", menuDto);
        } catch(MyBatisSystemException e) {
            log.error(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
