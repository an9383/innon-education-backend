package com.innon.education.admin.mypage.dao;

import com.innon.education.admin.mypage.repository.dto.SignCurrentDTO;
import com.innon.education.admin.mypage.repository.entity.MypageEntity;
import com.innon.education.admin.mypage.repository.model.Mypage;

import java.util.List;

public interface MypageDAO {
	int updateEduInsaInfo(MypageEntity entity);

	List<SignCurrentDTO> findSignCurrentDocumentList(Mypage entity);

	List<SignCurrentDTO> findSignCurrentEducationList(Mypage entity);

	List<SignCurrentDTO> findMyRequestSignInfo(Mypage entity);
}
