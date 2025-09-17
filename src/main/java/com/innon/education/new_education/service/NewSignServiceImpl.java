package com.innon.education.new_education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innon.education.new_education.dao.New_SignDAO;
import com.innon.education.new_education.dto.CurrentSignDTO;
import com.innon.education.new_education.dto.New_SearchDTO;

@Service
public class NewSignServiceImpl implements NewSignService {

	@Autowired
	New_SignDAO newSignDAO;

	@Override
	//전자결재 목록
	public List<CurrentSignDTO> currentSignList(New_SearchDTO searchDTO) throws Exception {
		return newSignDAO.currentSignList(searchDTO);
	}

	@Override
	// 이수철 : 미사용
	public List<CurrentSignDTO> planSignDetailList(New_SearchDTO searchDTO) {
		return newSignDAO.planSignDetailList(searchDTO);
	}
}
