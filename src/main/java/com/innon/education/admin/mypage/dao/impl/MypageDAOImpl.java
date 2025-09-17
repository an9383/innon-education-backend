package com.innon.education.admin.mypage.dao.impl;

import com.innon.education.admin.mypage.dao.MypageDAO;
import com.innon.education.admin.mypage.repository.dto.SignCurrentDTO;
import com.innon.education.admin.mypage.repository.entity.MypageEntity;
import com.innon.education.admin.mypage.repository.model.Mypage;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class MypageDAOImpl implements MypageDAO {

	@Autowired
	SqlSessionTemplate sqlSession;

	@Override
	public int updateEduInsaInfo(MypageEntity entity) {
		try {
			return sqlSession.update("com.innon.education.admin.mapper.mypage-mapper.updateEduInsaInfo", entity);
		}
		catch (MyBatisSystemException e) {
			log.error(e.getCause().getMessage());
			throw new MyBatisSystemException(e.getCause());
		}
	}

	@Override
	public List<SignCurrentDTO> findSignCurrentDocumentList(Mypage entity) {
		try {
			return sqlSession.selectList("com.innon.education.admin.mapper.mypage-mapper.findSignCurrentDocumentList", entity);
		}
		catch (MyBatisSystemException e) {
			log.error(e.getCause().getMessage());
			throw new MyBatisSystemException(e.getCause());
		}
	}

	@Override
	public List<SignCurrentDTO> findSignCurrentEducationList(Mypage entity) {
		try {
			return sqlSession.selectList("com.innon.education.admin.mapper.mypage-mapper.findSignCurrentEducationList", entity);
		}
		catch (MyBatisSystemException e) {
			log.error(e.getCause().getMessage());
			throw new MyBatisSystemException(e.getCause());
		}
	}

	@Override
	public List<SignCurrentDTO> findMyRequestSignInfo(Mypage entity) {
		try {
			return sqlSession.selectList("com.innon.education.admin.mapper.mypage-mapper.findMyRequestSignInfo", entity);
		}
		catch (MyBatisSystemException e) {
			log.error(e.getCause().getMessage());
			throw new MyBatisSystemException(e.getCause());
		}
	}

}
