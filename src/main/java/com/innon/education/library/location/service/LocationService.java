package com.innon.education.library.location.service;

import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.library.location.repository.model.Location;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface LocationService {
    ResultDTO saveLocation(Location location, HttpServletRequest request, Authentication auth);
    ResultDTO findLocationList(Location location, HttpServletRequest request, Authentication auth);

    ResultDTO updateLocationCodeList(Location location);
    ResultDTO deleteLocationCodeList(Location location);

    ResultDTO deleteTrashLocation();

    ResultDTO updateLocationCode(Location location, HttpServletRequest request, Authentication auth);

    ResultDTO deleteLocationCode(Location location, HttpServletRequest request, Authentication auth);
}
