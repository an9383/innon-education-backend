package com.innon.education.qualified.technology.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.innon.education.common.util.DataLib;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.qualified.technology.dao.TechnologyDAO;
import com.innon.education.qualified.technology.repository.dto.TechnologyDTO;
import com.innon.education.qualified.technology.repository.entity.TechnologyEntity;
import com.innon.education.qualified.technology.repository.entity.TechnologySearchEntity;
import com.innon.education.qualified.technology.repository.model.Technology;
import com.innon.education.qualified.technology.service.TechnologyService;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    private ResultDTO resultDTO;

    @Autowired
    TechnologyDAO technologyDAO;

    @Override
    public ResultDTO saveQualifiedTechnology(Technology technology) {
        resultDTO = new ResultDTO();
        TechnologyEntity entity = new TechnologyEntity(technology);
        int saveNum = technologyDAO.saveQualifiedTechnology(entity);
        if(saveNum > 0) {
            resultDTO.setMessage("직무기술 등록이 완료되었습니다.");
        } else {
            resultDTO.setMessage("직무기술 등록이 실패하였습니다.");
        }

        return resultDTO;
    }

    @Override
    public ResultDTO findQualifiedTechnologyList(Technology technology) {
        resultDTO = new ResultDTO();
        TechnologySearchEntity entity = new TechnologySearchEntity(technology);
        List<TechnologyDTO> resultList = technologyDAO.findQualifiedTechnologyList(entity);
        if(!DataLib.isEmpty(resultList)) {
            resultDTO.setState(true);
            resultDTO.setResult(resultList);
        } else {
            resultDTO.setState(false);
        }

        return resultDTO;
    }
}
