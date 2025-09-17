package com.innon.education.qualified.technology.service;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.qualified.technology.repository.model.Technology;

public interface TechnologyService {
    ResultDTO saveQualifiedTechnology(Technology technology);
    ResultDTO findQualifiedTechnologyList(Technology technology);
}
