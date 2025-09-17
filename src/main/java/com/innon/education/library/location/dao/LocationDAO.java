package com.innon.education.library.location.dao;

import com.innon.education.library.location.repository.dto.LocationDTO;
import com.innon.education.library.location.repository.entity.LocationEntity;
import com.innon.education.library.location.repository.model.Location;

import java.util.List;
import java.util.Map;

public interface LocationDAO {
    int saveLocationCode(Location location);
    Map<String, Object> saveExcelLocationCode(LocationEntity entity);
    List<LocationDTO> findLocationList(Location location);

    String findLocationCodeById(int id);
    LocationDTO findLocationCodeByCode(String codeName);
    int updateLocationCodeList(LocationEntity entity);
    int deleteLocationCodeList(LocationEntity entity);

    int deleteTrashLocation();

    int updateLocationCode(Location location);

    int deleteLocationCode(Location location);

    String locationFullPath(String locationCode);
}
