package com.innon.education.library.factory.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.library.factory.dao.FactoryDAO;
import com.innon.education.library.factory.repository.dto.FactoryDTO;
import com.innon.education.library.factory.repository.entity.FactoryEntity;
import com.innon.education.library.factory.repository.entity.FactorySearchEntity;
import com.innon.education.library.factory.repository.model.Factory;
import com.innon.education.library.factory.service.FactoryService;

@Service
public class FactoryServiceImpl implements FactoryService {

    private ResultDTO resultDTO;

    @Autowired
    FactoryDAO libraryFactoryDAO;

    @Autowired
    SignDAO signDAO;

    @Autowired
    MessageSource messageSource;

    @Override
    public ResultDTO findDisplayFactoryList(Factory factory) {
        resultDTO = new ResultDTO();
        FactorySearchEntity entity = new FactorySearchEntity(factory);
        List<FactoryDTO> resultList = libraryFactoryDAO.findDisplayFactoryList(entity);
        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }

    @Override
    public ResultDTO saveLocationDocument(List<Factory> factoryList) {
        resultDTO = new ResultDTO();
        int saveNum = 0;
        for(Factory factory : factoryList) {
            FactoryEntity entity = new FactoryEntity(factory);
            saveNum += libraryFactoryDAO.saveLocationDocument(entity);
        }

        if(saveNum > 0) {
            resultDTO.setMessage(saveNum + "건의 문서 입고가 완료 되었습니다.");
        } else {
            resultDTO.setMessage("입고 처리가 실패 하였습니다.");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO deleteTrashLocationDocument() {
        resultDTO = new ResultDTO();
        int deleteNum = libraryFactoryDAO.deleteTrashLocationDocument();
        if(deleteNum > 0) {
            resultDTO.setMessage("개발과정 TB_LOCATION_DOCUMENT 데이터 삭제 완료");
        } else {
            resultDTO.setMessage("TB_LOCATION_DOCUMENT 데이터 삭제 실패");
        }

        return resultDTO;
    }
}
