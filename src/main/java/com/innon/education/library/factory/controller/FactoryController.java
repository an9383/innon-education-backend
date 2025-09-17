package com.innon.education.library.factory.controller;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.library.factory.repository.model.Factory;
import com.innon.education.library.factory.service.FactoryService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/library/factory")
public class FactoryController {

    @Autowired
    FactoryService libraryFactoryService;

    /**
     * URL : /display/list
     * description : 서고 현황 조회
     * param : Factory
     * response : resultDTO
     */
    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findDisplayFactoryList(@RequestBody @Nullable Factory factory) {
        ResultDTO res = libraryFactoryService.findDisplayFactoryList(factory);
        return ResponseEntity.ok(res);
    }

    /**
     * URL : /save
     * description : 문서입고
     * param : List<Factory>
     * response : resultDTO
     */
    @PostMapping("/save")
    public ResponseEntity<ResultDTO> libraryFactoryDisplay(@RequestBody List<Factory> factoryList) {
        ResultDTO res = libraryFactoryService.saveLocationDocument(factoryList);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * 개발과정에서 데이터 삭제를 진행시키기 위한 API
     * @return
     */
    @DeleteMapping("/trash/delete")
    public ResponseEntity<ResultDTO> deleteLocationDocument() {
        ResultDTO res = libraryFactoryService.deleteTrashLocationDocument();
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
