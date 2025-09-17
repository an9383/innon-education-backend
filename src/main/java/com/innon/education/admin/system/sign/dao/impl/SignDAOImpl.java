package com.innon.education.admin.system.sign.dao.impl;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.*;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SignDAOImpl implements SignDAO {
    @Autowired
    SqlSessionTemplate sqlSession;

    @Override
    public int saveSign(Sign sign) {
        return sqlSession.insert("com.innon.education.admin.mapper.sign-mapper.saveSign", sign);
    }

    @Override
    public int saveSignUser(SignUser signUser) {
        return sqlSession.insert("com.innon.education.admin.mapper.sign-mapper.saveSignUser", signUser);
    }

    @Override
    public List<SignDTO> findSignList(Sign sign) {
        return sqlSession.selectList("com.innon.education.admin.mapper.sign-mapper.findSignList", sign);
    }

    @Override
    public List<SignUser> findSignUserList(int sign_id) {
        return sqlSession.selectList("com.innon.education.admin.mapper.sign-mapper.findSignUserList", sign_id);
    }

    @Override
    public SignDTO findSignById(int id) {
        return sqlSession.selectOne("com.innon.education.admin.mapper.sign-mapper.findSignById", id);
    }

    @Override
    public int savePlanSign(PlanSign planSign) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.sign-mapper.savePlanSign", planSign);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int saveExtensionPlanSign(PlanSign planSign) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.sign-mapper.saveExtensionPlanSign", planSign);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int savePlanSignManager(PlanSignManager planSignManager) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.sign-mapper.savePlanSignManager", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateSignApproveState(PlanSignManager planSignManager) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.sign-mapper.updateSignApproveState", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public PlanSignManager findTopApprover(PlanSignManager planSignManager) {
        try {
            return sqlSession.selectOne("com.innon.education.admin.mapper.sign-mapper.findTopApprover", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
	//기 입력 된 결재정보 중 가장 최근의 plan_sign_id 를 signform(교육계획/교육보고...) 기준으로 가져온다. (tb_plan_sign_manager)
    public List<PlanSignManager> findApproverList(PlanSignManager planSignManager) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.sign-mapper.findApproverList", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateSign(Sign sign) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.sign-mapper.updateSign", sign);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updateSignUser(SignUser signUser) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.sign-mapper.updateSignUser", signUser);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteSign(Sign sign) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.sign-mapper.deleteSign", sign);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int deleteSignUser(SignUser signUser) {
        try {
            return sqlSession.delete("com.innon.education.admin.mapper.sign-mapper.deleteSignUser", signUser);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    // 이수철 : 미사용
    public int savePlanSignDetail(PlanSignDetail planSignDetail) {
        try {
            return sqlSession.insert("com.innon.education.admin.mapper.sign-mapper.savePlanSignDetail", planSignDetail);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<PlanSignManager> findPlanSignManager(PlanSignManager planSignManager) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.sign-mapper.findPlanSignManager", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePlanSignManager(PlanSignManager planSignManager) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.sign-mapper.updatePlanSignManager", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int updatePlanSign(PlanSign planSign) {
        try {
            return sqlSession.update("com.innon.education.admin.mapper.sign-mapper.updatePlanSign", planSign);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public int findPlanSignState(int plan_id) {
        try {
            return sqlSession.selectOne("com.innon.education.admin.mapper.sign-mapper.findPlanSignState", plan_id);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public PlanSign findPlanSign(PlanSign paramSign) {
        try {
            return sqlSession.selectOne("com.innon.education.admin.mapper.sign-mapper.findPlanSign", paramSign);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            throw new MyBatisSystemException(e.getCause());
        }
    }

    @Override
    public List<PlanSignManager> findPlanSignManagerExtensionRequest(PlanSignManager planSignManager) {
        try {
            return sqlSession.selectList("com.innon.education.admin.mapper.sign-mapper.findPlanSignManagerExtensionRequest", planSignManager);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause().getMessage());
            throw new MyBatisSystemException(e.getCause());
        }
    }
}
