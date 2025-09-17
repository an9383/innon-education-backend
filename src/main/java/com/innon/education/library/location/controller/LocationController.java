package com.innon.education.library.location.controller;

import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.library.location.repository.model.Location;
import com.innon.education.library.location.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@CrossOrigin
@RestController
@RequestMapping("/api/library/location")
public class LocationController {

    @Autowired
    private LocationService locationService;
    @Autowired
    MessageSource messageSource;

    @PostMapping("/save")
    public ResponseEntity<ResultDTO> saveLocation(@RequestBody Location location, HttpServletRequest request, Authentication auth) {
        ResultDTO res = locationService.saveLocation(location, request, auth);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<ResultDTO> findLocationList(@RequestBody Location location, HttpServletRequest request, Authentication auth) {
        ResultDTO res = locationService.findLocationList(location, request, auth);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/update")
    public ResponseEntity<ResultDTO> updateLocationCodeList(@RequestBody Location location, HttpServletRequest request, Authentication auth) {
//        ResultDTO res = locationService.updateLocationCodeList(location);
        ResultDTO res = new ResultDTO();
        try {
            res = locationService.updateLocationCode(location, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치코드 수정"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/delete")
    public ResponseEntity<ResultDTO> deleteLocationCodeList(@RequestBody Location location, HttpServletRequest request, Authentication auth) {
//        ResultDTO res = locationService.deleteLocationCodeList(location);
        ResultDTO res = new ResultDTO();
        try {
            res = locationService.deleteLocationCode(location, request, auth);
        } catch (Exception e) {
            System.out.println(e.getCause());
            res.setState(false);
            res.setCode(400);
            res.setMessage(messageSource.getMessage("api.message.400", new String[]{"위치코드 삭제"}, Locale.KOREA));
        }

        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    /**
     * 개발과정에서 데이터 삭제를 진행시키기 위한 API
     * @return
     */
    @DeleteMapping("/trash/delete")
    public ResponseEntity<ResultDTO> deleteTrashLocation() {
        ResultDTO res = locationService.deleteTrashLocation();
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
}
