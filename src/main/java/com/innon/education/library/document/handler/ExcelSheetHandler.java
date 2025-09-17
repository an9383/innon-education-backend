package com.innon.education.library.document.handler;

import com.innon.education.common.util.DataLib;
import com.innon.education.library.document.repasitory.entity.DocumentEntity;
import com.innon.education.library.location.repository.entity.LocationEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.springframework.security.core.Authentication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class ExcelSheetHandler implements XSSFSheetXMLHandler.SheetContentsHandler {
    private int currentCol = -1;
    private int currRowNum = 0;
    private List<String> row = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();    // 실제 엑셀을 파싱해서 담아지는 데이터
    private List<String> header = new ArrayList<>();
    private String sheetType = "";
    private List<Map<String, String>> rowsWithHeader = new ArrayList<>();
    private Integer firstRowIndex = 0;

//    private List<Map<String, Object>> insertRow = new ArrayList<>();
    private Map<String, Object> insertMap = new HashMap<>();
    private List<LocationEntity> insertRow = new ArrayList<>();
    private List<DocumentEntity> documentRow = new ArrayList<>();

    @Override
    public void startRow(int rowNum) {
        this.currentCol = -1;
        this.currRowNum = rowNum;
    }

    @Override
    public void endRow(int rowNum) {

        if (rowNum == 2) {
            header = new ArrayList<>(row);
        } else {
            if (row.size() < header.size()) {
                for (int i = row.size(); i < header.size(); i++) {
                    row.add("");
                }
            }
            rows.add(new ArrayList<>(row));
//            insertRow.addAll(setInsertList(insertMap));
            insertRow.addAll(setInsertLocationEntity(insertMap, null));
            documentRow.addAll(setInsertDocumentEntity(insertMap, null));
        }

        row.clear();
        insertMap.clear();
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        int iCol = (new CellReference(cellReference)).getCol() - 1;
        String colNm = setColName(cellReference);
        int emptyCol = iCol - currentCol - 1;

        for (int i = 0; i < emptyCol; i++) {
            row.add("");
        }
        currentCol = iCol;
        row.add(formattedValue);
        insertMap.put(colNm, formattedValue);
    }

    // 서고, 랙의 order_num을 구하기 위한 함수
    public static int convertToNum(String str, String type) {
        int result = 0;
        if(type.equals("document")) {
            if(str.length() > 2) {
                return 0;
            }
            for(int i = 0; i < str.length(); i++) {
                result *= 26;
                result += (str.charAt(i) - 'A' + 1);
            }
        } else if(type.equals("rack")) {
            int front_num = (str.charAt(0) - 'A');
            String regExp = "^[0-9]+$";
            int back_num = 0;
            if(str.substring(1).matches(regExp)) {
                back_num = Integer.parseInt(str.substring(1));
            } else {
                return 0;
            }
            result = (99 * front_num) + back_num;
        }

        return result;
    }

    // row별 공장, 서고, 층계, 랙 값 셋팅
    public static List<LocationEntity> setInsertLocationEntity(Map<String, Object> insertMap, Authentication auth) {
        List<LocationEntity> locationEntities = new ArrayList<>();
        String plant_cd = insertMap.get("") != null ? insertMap.get("group_id").toString() : null;
        String documentCode = insertMap.get("document") != null ? insertMap.get("document").toString() : null;
        String documentName = insertMap.get("document") != null ? insertMap.get("document").toString() : null;
        if(documentCode != null && plant_cd != null) {
            documentCode = plant_cd + documentCode;
        }

        String floorCode = insertMap.get("floor") != null ? insertMap.get("floor").toString() : null;
        String floorName = insertMap.get("floor") != null ? insertMap.get("floor").toString() : null;
        if(floorCode != null && documentCode != null) {
            floorCode = documentCode + floorCode;
        }

        String rackCode = insertMap.get("rack") != null ?  insertMap.get("rack").toString() : null;
        String rackName = insertMap.get("rack") != null ?  insertMap.get("rack").toString() : null;
        if(rackCode != null && floorCode != null) {
            rackCode = floorCode + rackCode;
        }
        String locatioName = insertMap.get("location_name") != null ? insertMap.get("location_name").toString() : null;
        String labParentCode = rackCode != null ? rackCode :
                floorCode != null ? floorCode :
                documentCode != null ? documentCode : plant_cd;
        Map<String, String> factoryMap = new HashMap<>();
        factoryMap.put("1900", "오송공장");
        factoryMap.put("1052", "이천공장");
        factoryMap.put("1080", "대소공장");
        factoryMap.put("1300", "CDMO");

        int depth_level = 0;

        // 공장일 경우 location_code에 따라 location_name 지정
        // reg_user_id는 엑셀 리드시 엑셀 등록자 기준으로 아이디 가져옴
        LocationEntity companyLocation = null;
        if(plant_cd != null) {
            companyLocation = new LocationEntity(
                    auth.getName(),
                    null,
                    factoryMap.getOrDefault(plant_cd, "공장"),
                    plant_cd,
                    0,
                    plant_cd,
                    factoryMap.getOrDefault(plant_cd, "공장"),
                    'Y',
                    depth_level++,
                    'N',
                    ""
            );
        }

        LocationEntity documentEntity = null;
        if(documentCode != null) {
            documentEntity = new LocationEntity(
                    auth.getName(),
                    plant_cd,
                    factoryMap.getOrDefault(plant_cd, "공장") + "_" + documentName,
                    documentCode,
                    convertToNum(insertMap.get("document").toString(), "document"),
                    plant_cd,
                    documentName,
                    'Y',
                    depth_level++,
                    'N',
                    ""
            );
        }

        LocationEntity floorEntity = null;
        if(floorCode != null) {
            String regExp = "^[0-9]+$";
            int floor_order = 0;
            if(insertMap.get("floor").toString().matches(regExp)) {
                floor_order = Integer.parseInt(insertMap.get("floor").toString());
            }
            floorEntity = new LocationEntity(
                    auth.getName(),
                    documentCode,
                    factoryMap.getOrDefault(plant_cd, "공장") + "_" + documentName + "_" + floorName,
                    floorCode,
                    floor_order,
                    plant_cd,
                    floorName,
                    'Y',
                    depth_level++,
                    'N',
                    ""
            );
        }

        LocationEntity rackEntity = null;
        if(rackCode != null) {
            rackEntity = new LocationEntity(
                    auth.getName(),
                    floorCode,
                    factoryMap.getOrDefault(plant_cd, "공장") + "_" +documentName + "_" + floorName + "_" + rackName,
                    rackCode,
                    convertToNum(insertMap.get("rack").toString(), "rack"),
                    plant_cd,
                    rackName,
                    'Y',
                    depth_level++,
                    'N',
                    ""
            );
        }
        if(companyLocation != null) {
            locationEntities.add(companyLocation);
        }
        if(documentEntity != null) {
            locationEntities.add(documentEntity);
        }
        if(floorEntity != null) {
            locationEntities.add(floorEntity);
        }
        if(rackEntity != null) {
            locationEntities.add(rackEntity);
        }

        LocationEntity labEntity = null;
        if(locatioName != null) {
            String lab_code = labParentCode != null ? labParentCode + "LABLOCATION" : "LABLOCATION";
            labEntity = new LocationEntity(
                    auth.getName(),
                    labParentCode,
                    locatioName,
                    lab_code,
                    0,
                    plant_cd,
                    locatioName,
                    'Y',
                    depth_level++,
                    'N',
                    ""
            );
            locationEntities.add(labEntity);
        }

        return locationEntities;
    }

    // 문서 데이터 등록을 위한 값 셋팅
    public static List<DocumentEntity> setInsertDocumentEntity(Map<String, Object> insertMap, Authentication auth) {
        List<DocumentEntity> documentEntities = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String plant_cd = insertMap.get("plant_cd") != null ? insertMap.get("plant_cd").toString() : null;
        String document_num = insertMap.getOrDefault("document_num", "").toString();
        String title = insertMap.getOrDefault("title", "").toString();
        String revised_num = insertMap.getOrDefault("revised_num", "").toString();
        String reg_user_id = insertMap.getOrDefault("reg_user_id", "").toString();
        Date cofirm_date = null;
        String code_id = insertMap.getOrDefault("code_id", "").toString();
        String write_year = insertMap.getOrDefault("write_year", "").toString();
        String write_date = insertMap.getOrDefault("write_date", "").toString();
        String write_user_id = insertMap.getOrDefault("write_user_id", "").toString();
        String etc = insertMap.getOrDefault("etc", "").toString();
        String document_cnt = insertMap.getOrDefault("document_cnt", "").toString();
        String qr_code = insertMap.getOrDefault("qr_code", "").toString();
        String machineNo = insertMap.getOrDefault("machineNo", "").toString();
        String test_num = insertMap.getOrDefault("test_num", "").toString();

        try {
            String strDate = insertMap.get("confirm_date") != null ?
                    String.valueOf(insertMap.get("confirm_date")).replaceAll("\\.", "-") : null;
            if(!DataLib.isEmpty(strDate)) {
                cofirm_date = format.parse(strDate);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setPlant_cd(plant_cd);
        documentEntity.setDocument_num(document_num);
        documentEntity.setTitle(title);
        documentEntity.setRevised_num(revised_num);
        documentEntity.setConfirm_date(cofirm_date);
        documentEntity.setReg_user_id(reg_user_id);
        documentEntity.setCode_id(code_id);
        documentEntity.setWrite_year(write_year);
        documentEntity.setWrite_date(write_date);
        documentEntity.setWrite_user_id(write_user_id);
        documentEntity.setEtc(etc);
        documentEntity.setDocument_cnt(document_cnt);
        documentEntity.setQr_code(qr_code);
        documentEntity.setMachineNo(machineNo);
        documentEntity.setTest_num(test_num);
        documentEntities.add(documentEntity);

        return documentEntities;
    }

    private static String setColName(String cellReference) {
        String colName = "";

        if(cellReference.indexOf('D') > -1) {
            colName = "document_num";
        } else if(cellReference.indexOf('E') > -1) {
            colName = "title";
        } else if(cellReference.indexOf('F') > -1) {
            colName = "revised_num";
        } else if(cellReference.indexOf('G') > -1) {
            colName = "confirm_date";
        } else if(cellReference.indexOf('I') > -1) {
            colName = "reg_user_id";
        }else if(cellReference.indexOf('J') > -1) {
            colName = "document";
        } else if(cellReference.indexOf('K') > -1) {
            colName = "floor";
        } else if(cellReference.indexOf('L') > -1) {
            colName = "rack";
        } else if(cellReference.indexOf('M') > -1) {
            colName = "company";
        } else if(cellReference.indexOf('N') > -1) {
            colName = "description";
        } else {
            colName = cellReference;
        }

        return colName;
    }
}
