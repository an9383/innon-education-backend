package com.innon.education.library.factory.service;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.library.factory.repository.model.Factory;

import java.util.List;

public interface FactoryService {
    ResultDTO findDisplayFactoryList(Factory factory);
    ResultDTO saveLocationDocument(List<Factory> factoryList);

    ResultDTO deleteTrashLocationDocument();
}
