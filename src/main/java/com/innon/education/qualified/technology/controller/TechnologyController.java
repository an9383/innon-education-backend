package com.innon.education.qualified.technology.controller;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.qualified.technology.repository.model.Technology;
import com.innon.education.qualified.technology.service.TechnologyService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/education/qualified/technology")
public class TechnologyController {

    @Autowired
    private TechnologyService technologyService;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveQualifiedTech(@RequestBody Technology technology) {
        ResultDTO res = technologyService.saveQualifiedTechnology(technology);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findQualifiedTechList(@RequestBody @Nullable Technology technology) {
        ResultDTO res = technologyService.findQualifiedTechnologyList(technology);
        return ResponseEntity.ok(res);
    }
}
