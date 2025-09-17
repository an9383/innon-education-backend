package com.innon.education.qualified.technology.dao;

import com.innon.education.qualified.technology.repository.dto.TechnologyDTO;
import com.innon.education.qualified.technology.repository.entity.TechnologyEntity;
import com.innon.education.qualified.technology.repository.entity.TechnologySearchEntity;

import java.util.List;

public interface TechnologyDAO {
    int saveQualifiedTechnology(TechnologyEntity entity);
    List<TechnologyDTO> findQualifiedTechnologyList(TechnologySearchEntity entity);
}
