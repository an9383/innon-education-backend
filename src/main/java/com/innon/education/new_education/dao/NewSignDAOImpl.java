package com.innon.education.new_education.dao;

import com.innon.education.new_education.dto.CurrentSignDTO;
import com.innon.education.new_education.dto.New_SearchDTO;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewSignDAOImpl implements New_SignDAO {

    @Autowired
    SqlSessionTemplate sqlSession;
    @Override
    public List<CurrentSignDTO> currentSignList(New_SearchDTO searchDTO) throws Exception {
        return sqlSession.selectList("com.innon.education.new_education.mapper.new_sign-mapper.currentSignList", searchDTO);
    }

    @Override
    //이수철 : 미사용
    public List<CurrentSignDTO> planSignDetailList(New_SearchDTO searchDTO) {
        return sqlSession.selectList("com.innon.education.new_education.mapper.new_sign-mapper.planSignDetailList", searchDTO);
    }
}
