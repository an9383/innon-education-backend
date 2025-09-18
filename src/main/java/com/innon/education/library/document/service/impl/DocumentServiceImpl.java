package com.innon.education.library.document.service.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.innon.education.admin.doccode.dao.DocCodeDAO;
import com.innon.education.admin.doccode.repository.DocCode;
import com.innon.education.admin.doccode.repository.DocCodeDTO;
import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.auth.entity.Role;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.dto.LogDTO;
import com.innon.education.common.repository.entity.LogChild;
import com.innon.education.common.repository.entity.LogEntity;
import com.innon.education.common.repository.model.Code;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.code.controller.dao.CodeDAO;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.handler.ExcelSheetHandler;
import com.innon.education.library.document.repasitory.dto.ApproveMyworkDTO;
import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.entity.DocumentApplyEntity;
import com.innon.education.library.document.repasitory.entity.DocumentEntity;
import com.innon.education.library.document.repasitory.entity.DocumentLoanApproveEntity;
import com.innon.education.library.document.repasitory.entity.DocumentLoanEntity;
import com.innon.education.library.document.repasitory.entity.DocumentSearchEntity;
import com.innon.education.library.document.repasitory.model.Document;
import com.innon.education.library.document.repasitory.model.DocumentMemo;
import com.innon.education.library.document.service.DocumentService;
import com.innon.education.library.factory.dao.FactoryDAO;
import com.innon.education.library.factory.repository.entity.FactoryEntity;
import com.innon.education.library.factory.repository.model.Factory;
import com.innon.education.library.location.dao.LocationDAO;
import com.innon.education.library.location.repository.dto.LocationDTO;
import com.innon.education.library.location.repository.entity.LocationEntity;
import com.innon.education.library.location.repository.model.Location;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.entity.WorkManagementEntity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {

	private ResultDTO resultDTO;

	@Autowired
	DocumentDAO documentDAO;

	@Autowired
	LocationDAO locationDAO;

	@Autowired
	FactoryDAO factoryDAO;

	@Autowired
	ManagementPlanDAO planDAO;

	@Autowired
	CodeDAO codeDAO;

	@Autowired
	CommonDAO commonDAO;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MessageSource messageSource;

	@Autowired
	CommonService commonService;

	@Autowired
	SignDAO signDAO;

	@Autowired
	DocCodeDAO docCodeDAO;

	@Override
	public ResultDTO findDocumentList(Document document, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();
		boolean chkUserRole = auth.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		if(!chkUserRole) {
			document.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
		}
		DocumentSearchEntity entity = new DocumentSearchEntity(document);
		if(document.getPage() != null) {
			entity.setPage(document.getPage());
		}

		List<DocumentDTO> resultList = documentDAO.findDocumentList(entity);
		if(document.getPage() != null) {
			resultDTO.setPage(document.getPage());
			resultDTO.getPage().setTotalCnt(resultList);
		}

		if(!DataLib.isEmpty(resultList)) {
			resultDTO.setState(true);
			resultDTO.setResult(resultList);
			// resultMessage 추가
		} else {
			resultDTO.setState(false);
		}

		return resultDTO;
	}
	@Transactional
	@Override
	public ResultDTO saveDocument(Document document, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(document.getUser(), request, auth);

		if(resultDTO.getCode() == 200) {
			try {
				User user = (User) auth.getPrincipal();
				document.setReg_user_id(auth.getName());

				DocumentEntity entity = new DocumentEntity(document);
				String write_year = document.getWrite_year() == null ? "" : document.getWrite_year() + '|';
				String document_cnt = document.getDocument_cnt() == null ? "" : document.getDocument_cnt();
				String qr_code = document.getDoc_code() + '|' +
						document.getDocument_num() + '|' +
						document.getTitle() + '|' +
						document.getWrite_user_id() + '|' +
						write_year + document_cnt;
				entity.setQr_code(qr_code);

				int resultId = documentDAO.saveDocument(entity);

				if (resultId > 0) {
					// 문서 비고 등록
					DocumentMemo memo = new DocumentMemo();
					memo.setDocument_id(entity.getId());
					memo.setMemo(document.getMemo());
					memo.setSys_reg_user_id(auth.getName());
					memo.setType("doc");
					documentDAO.saveDocumentMemo(memo);

					int logId = commonService.saveLog(LogEntity.builder()
							.table_id(memo.getId())
							.type("doc")
							.page_nm("document")
							.url_addr(request.getRequestURI())
							.state("save")
							.reg_user_id(auth.getName())
							.build());
					if (logId > 0) {
						try {
							Object obj = entity;
							for (Field field : obj.getClass().getDeclaredFields()) {
								field.setAccessible(true);
								Object value = field.get(obj);
								if (value != null) {
									commonService.saveLogDetail(LogChild
											.builder()
											.log_id(logId)
											.field(field.getName())
											.new_value(value.toString())
											.reg_user_id(auth.getName())
											.build());
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					DocumentLoanEntity loanEntity = new DocumentLoanEntity();
					loanEntity.setD_id(entity.getId());
					loanEntity.setSys_reg_user_id(auth.getName());
					String code_name = "docs11003";

					int locationLog = commonService.saveLog(LogEntity.builder()
							.table_id(memo.getId())
							.type("doc")
							.page_nm("loan")
							.url_addr(request.getRequestURI())
							.state("save")
							.reg_user_id(auth.getName())
							.build());

					if (!StringUtils.defaultString(document.getLocation_code()).equals("")) {
						loanEntity.setStatus(code_name);

						// 코드 값 조회 후 문서위치 코드 값 설정
						LocationDTO location = locationDAO.findLocationCodeByCode(document.getLocation_code());

						// 문서 위치 등록
						FactoryEntity factoryEntity = new FactoryEntity();
						factoryEntity.setDocument_id(entity.getId());
						factoryEntity.setLocation_id(location.getId());
						factoryEntity.setDisplay_user_id(document.getWrite_user_id());
						factoryEntity.setDisplay_date(entity.getDisplay_date());
						factoryEntity.setSys_reg_user_id(auth.getName());
						factoryDAO.saveLocationDocument(factoryEntity);


						String fullPath = locationDAO.locationFullPath(location.getLocation_code());
						commonService.saveLogDetail(LogChild
								.builder()
								.log_id(locationLog)
								.field("location")
								.new_value(fullPath)
								.reg_user_id(auth.getName())
								.build());

						CodeDTO re_code = codeDAO.findByCodeName(code_name);
						commonService.saveLogDetail(LogChild
								.builder()
								.log_id(locationLog)
								.field("status")
								.new_value(re_code.getDiscription())
								.reg_user_id(auth.getName())
								.build());
						// 문서 현황 등록
						documentDAO.saveDocumentLoan(loanEntity);
					} else {
						code_name = "docs11004";
						loanEntity.setStatus(code_name);

						CodeDTO re_code = codeDAO.findByCodeName(code_name);
						commonService.saveLogDetail(LogChild
								.builder()
								.log_id(locationLog)
								.field("status")
								.new_value(re_code.getDiscription())
								.reg_user_id(auth.getName())
								.build());
						// 문서 현황 등록
						documentDAO.saveDocumentLoan(loanEntity);
					}
					resultDTO.setState(true);
					resultDTO.setCode(201);
					resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서"}, Locale.KOREA));
					resultDTO.setResult(entity.getId());
				} else {
					resultDTO.setState(false);
					resultDTO.setCode(400);
					resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서"}, Locale.KOREA));
					resultDTO.setResult(null);
				}
			}catch (Exception e){
				e.printStackTrace();
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서"}, Locale.KOREA));
				resultDTO.setResult(null);
			}

		}
			return resultDTO;

	}

	@Transactional
	@Override
	public ResultDTO updateDocument(Document document, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(document.getUser(), request, auth);
		if(resultDTO.getCode() == 200) {
			User user = (User) auth.getPrincipal();
			document.setReg_user_id(auth.getName());
			//이수철 추가
			String expireDate = (DataLib.toNnTrim(document.getExpire_date()).equals("영구보관")) ? null : DataLib.toNN(document.getExpire_date(),null);
			document.setExpire_date(expireDate);
			
			DocumentEntity entity = new DocumentEntity(document);
			DocumentEntity beforeEntity = new DocumentEntity(document.getBefore_document());
			try {
				int cnt = documentDAO.updateDocument(entity);
				if (cnt > 0) {
					// 문서 비고 등록
					DocumentMemo memo = new DocumentMemo();
					memo.setDocument_id(entity.getId());
					memo.setMemo(document.getMemo());
					memo.setSys_reg_user_id(auth.getName());
					memo.setType("doc");
					documentDAO.saveDocumentMemo(memo);
					
					//S : 수정 로그저장 ----------------------------------------------------------------------------------------- 
					cnt = commonService.saveLog(LogEntity.builder()
							.type("doc")
							.table_id(memo.getId())
							.page_nm("document")
							.url_addr(request.getRequestURI())
							.state("update")
							.reg_user_id(auth.getName())
							.build());
					if (cnt > 0) {
						try {
							Object obj = entity;
							Object obj2 = beforeEntity;
							//변경 정보 저장
							for (Field field : obj.getClass().getDeclaredFields()) {
								field.setAccessible(true);
								Object value = field.get(obj);
								Object value2 = field.get(obj2);
								if (value != null && !value.equals(value2)) {
									String value2Str = value2 == null ? "" : value2.toString();
									commonService.saveLogDetail(LogChild
											.builder()
											.log_id(cnt)
											.field(field.getName())
											.new_value(value.toString())
											.before_value(value2Str)
											.reg_user_id(auth.getName())
											.build());
								}
							}

							// 상속받은 변경 정보 저장
							for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
								field.setAccessible(true);
								Object value = field.get(obj);
								Object value2 = field.get(obj2);
								if (value != null && !value.equals(value2)) {
									commonService.saveLogDetail(LogChild
											.builder()
											.log_id(cnt)
											.field(field.getName())
											.new_value(value.toString())
											.before_value(value2.toString())
											.reg_user_id(auth.getName())
											.build());
								}
							}
						} catch (Exception e) {
							System.out.println(e.getCause().toString());
							e.printStackTrace();
						}
					}
					//E : 수정 로그저장 ----------------------------------------------------------------------------------------- 
					
					String beforeLocationCode = StringUtils.defaultString(document.getBefore_document().getLocation_code());

					// 보관위치 수정시
					if(!(beforeLocationCode).equals(document.getLocation_code())) {
						int locationLog = commonService.saveLog(LogEntity.builder()
								.type("doc")
								.table_id(memo.getId())
								.page_nm("loan")
								.url_addr(request.getRequestURI())
								.state("update")
								.reg_user_id(auth.getName())
								.build());

						if (!StringUtils.defaultString(document.getLocation_code()).equals("")) {
							// DocumentSearchEntity documentSearchEntity = new DocumentSearchEntity(document);
							// DocumentDTO documentDTO = documentDAO.getDocumentLoan(documentSearchEntity);

							FactoryEntity factoryCntEntity = new FactoryEntity();
							factoryCntEntity.setDocument_id(entity.getId());
							int locationCnt = factoryDAO.findLocationDocumentCnt(factoryCntEntity);

							DocumentApplyEntity docLoanCntEntity = new DocumentApplyEntity();
							docLoanCntEntity.setDocument_id(entity.getId());

							// 코드 값 조회 후 문서위치 코드 값 설정
							LocationDTO location = locationDAO.findLocationCodeByCode(document.getLocation_code());
							FactoryEntity factoryEntity = new FactoryEntity();

							if (locationCnt > 0) {
								// 문서 위치 수정
								factoryEntity.setDocument_id(entity.getId());
								factoryEntity.setLocation_id(location.getId());
								factoryEntity.setDisplay_user_id(document.getWrite_user_id());
								factoryEntity.setSys_reg_user_id(auth.getName());
								factoryEntity.setDisplay_date(entity.getDisplay_date());
								factoryDAO.updateLocationDocument(factoryEntity);
								String beforeFullPath = locationDAO.locationFullPath(document.getBefore_document().getLocation_code());
								String fullPath = locationDAO.locationFullPath(location.getLocation_code());
								commonService.saveLogDetail(LogChild
										.builder()
										.log_id(locationLog)
										.field("location")
										.before_value(beforeFullPath)
										.new_value(fullPath)
										.reg_user_id(auth.getName())
										.build());
							} else {
								// 문서 위치 등록
								factoryEntity.setDocument_id(entity.getId());
								factoryEntity.setLocation_id(location.getId());
								factoryEntity.setDisplay_user_id(document.getWrite_user_id());
								factoryEntity.setDisplay_date(entity.getDisplay_date());
								factoryEntity.setSys_reg_user_id(auth.getName());
								factoryDAO.saveLocationDocument(factoryEntity);
								String fullPath = locationDAO.locationFullPath(location.getLocation_code());
								commonService.saveLogDetail(LogChild
										.builder()
										.log_id(locationLog)
										.field("location")
										.new_value(fullPath)
										.reg_user_id(auth.getName())
										.build());
							}

							int loanCnt = factoryDAO.findDocumentLoanCntByDocId(docLoanCntEntity);
							if (loanCnt > 0) {
								// 문서 현황 수정
								DocumentApplyEntity applyEntity = new DocumentApplyEntity();
								applyEntity.setDocument_id(entity.getId());
								applyEntity.setStatus("docs11003");//입고
								applyEntity.setSys_reg_user_id(auth.getName());
								factoryDAO.updateDocumentLoan(applyEntity);

							} else {
								// 문서 현황 등록
								DocumentLoanEntity loanEntity = new DocumentLoanEntity();
								loanEntity.setD_id(entity.getId());
								loanEntity.setStatus("docs11004");//작성
								loanEntity.setSys_reg_user_id(auth.getName());
								documentDAO.saveDocumentLoan(loanEntity);
							}

							CodeDTO old_code = codeDAO.findByCodeName(document.getBefore_document().getStatus());
							CodeDTO re_code = codeDAO.findByCodeName("docs11003");
							String oldCode = old_code == null ? "" : old_code.getDiscription();
							commonService.saveLogDetail(LogChild
									.builder()
									.log_id(locationLog)
									.field("status")
									.before_value(oldCode)
									.new_value(re_code.getDiscription())
									.reg_user_id(auth.getName())
									.build());
						}
						else if (DataLib.isEmpty(document.getLocation_code()) && !DataLib.isEmpty(document.getBefore_document().getLocation_code())) {
							DocumentApplyEntity documentApplyEntity = new DocumentApplyEntity();
							documentApplyEntity.setDocument_id(entity.getId());
							documentApplyEntity.setStatus("docs11004");
							documentApplyEntity.setSys_reg_user_id(auth.getName());
							factoryDAO.updateDocumentLoan(documentApplyEntity);//입고 -> 보관
							FactoryEntity delFactory = new FactoryEntity();
							delFactory.setDelete_at('Y');
							delFactory.setDocument_id(entity.getId());
							factoryDAO.updateLocationDocument(delFactory);


							String beforeFullPath = locationDAO.locationFullPath(document.getBefore_document().getLocation_code());
							commonService.saveLogDetail(LogChild
									.builder()
									.log_id(locationLog)
									.field("location")
									.before_value(beforeFullPath)
									.new_value("")
									.reg_user_id(auth.getName())
									.build());

							CodeDTO old_code = codeDAO.findByCodeName(document.getBefore_document().getStatus());
							CodeDTO re_code = codeDAO.findByCodeName("docs11004");
							commonService.saveLogDetail(LogChild
									.builder()
									.log_id(locationLog)
									.field("status")
									.before_value(old_code.getDiscription())
									.new_value(re_code.getDiscription())
									.reg_user_id(auth.getName())
									.build());
						}
					}


					resultDTO.setState(true);
					resultDTO.setCode(202);
					resultDTO.setMessage(messageSource.getMessage("api.message.202", new String[]{"문서", "수정"}, Locale.KOREA));
					resultDTO.setResult(entity.getId());
				} else {
					resultDTO.setState(false);
					resultDTO.setCode(400);
					resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서"}, Locale.KOREA));
					resultDTO.setResult(null);
				}
			}catch (Exception e){
				e.printStackTrace();
				log.info(e.getCause().toString());
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서"}, Locale.KOREA));
				resultDTO.setResult(null);
			}

		}
		return resultDTO;
	}

	public ResultDTO saveDocument_back(Document document, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		int saveNum = 0;
		int updateNum = 0;

		// 넘어온 plant_cd가 없을경우 등록자의 plant_cd로 등록
		User user = (User) auth.getPrincipal();

		DocumentEntity entity = new DocumentEntity(document);
		if(document.getId() > 0) {
			updateNum += documentDAO.updateDocument(entity);
			if(updateNum > 0) {
				/*int logId = commonService.saveLog(LogEntity.builder()
						.table_id(entity.getId())
						.page_nm("adminDocument")
						.url_addr(request.getRequestURI())
						.state("update")
						.reg_user_id(auth.getName())
						.build());
				if(logId > 0) {
					try {
						Object obj = entity;
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							Object value = field.get(obj);
							if(value != null) {
								commonService.saveLogDetail(LogChild
										.builder()
										.log_id(logId)
										.field(field.getName())
										.new_value(value.toString())
										.reg_user_id(auth.getName())
										.build());
							}
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}*/
			}



		} else {
			/*Map<String, Object> documentMap = documentDAO.saveDocument(entity);
			String document_status = documentMap.get("status").toString();
			if (document_status.equals("INSERT")) {
				saveNum++;
			}
			//todo 정주원 문서의 관한 코멘트가 필요
			if(!document.getComments().equals("")){
				// ,
				DocumentMemo memo = new DocumentMemo(document, document.getId());
				memo.setSys_reg_user_id(auth.getName());
				documentDAO.saveDocumentMemo(memo);
			}*/
		}

		if(saveNum > 0) {
			resultDTO.setMessage("문서 등록이 완료 되었습니다.");
		} else if(updateNum > 0) {
			resultDTO.setMessage("문서 수정이 완료 되었습니다.");
		} else {
			resultDTO.setMessage("요청하신 처리가 실패 하였습니다.");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO saveDocumentApply(List<Document> documentList, Authentication auth) {
		resultDTO = new ResultDTO();
		int saveNum = 0;
		int workSaveNum = 0;
		int approveSaveNum = 0;

		for(Document document : documentList) {
			// 코드 값 조회 후 요청 상태 값 설정
			CodeDTO code = codeDAO.findByCodeName(document.getStatus());

			// 문서현황 등록 또는 수정
			DocumentApplyEntity entity = new DocumentApplyEntity(document, auth.getName());
			entity.setStatus(code.getCode_name());
			int document_loan_id = documentDAO.saveDocumentApply(entity);
			if(document_loan_id > 0) {
				saveNum++;
				DocumentMemo memo = new DocumentMemo(document, document_loan_id);
				memo.setSys_reg_user_id(auth.getName());
				memo.setType("doc");
				documentDAO.saveDocumentMemo(memo);
			}

			// 나의 업무 정보 등록
			WorkManagementEntity workEntity = new WorkManagementEntity(
					document_loan_id,
					code.getDiscription(),
					auth.getName()
			);
			workSaveNum += planDAO.saveWorkManagement(workEntity);

			// 승인권자 조회
			String searchRoleStr = "'LIBRARY_MANAGER','LIBRARY_ADMIN'";
			List<Role> roleList = userRepository.findRoleUserIdByName(searchRoleStr);

			// 승인권자 등록
			for(Role role : roleList) {
				DocumentLoanApproveEntity approveEntity = new DocumentLoanApproveEntity(
						document_loan_id,
						role.getId(),
						code.getDiscription()
				);
				approveSaveNum += documentDAO.saveDocumentLoanApprove(approveEntity);
			}
		}

		if(saveNum > 0) {
			resultDTO.setMessage(saveNum + "건의 문서 신청이 완료 되었습니다. " +
					workSaveNum + "건의 나의업무 등록이 완료 되었습니다. " +
					approveSaveNum + "건의 승인권자 등록이 완료 되었습니다.");
		} else {
			resultDTO.setMessage("문서 신청이 실패하였습니다.");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO readExcel() {
		// 입고처리 작업 안됨 처리 해야함
		resultDTO = new ResultDTO();
		Map<String, Integer> cntMap = new HashMap<>();
		String filePath = "C:\\Users\\Administrator\\Desktop\\readFolder\\통합 문서2.xlsx";

		try {
			OPCPackage opcPackage = OPCPackage.open(filePath);
			XSSFReader xssfReader = new XSSFReader(opcPackage);
			StylesTable styles = xssfReader.getStylesTable();
			SharedStrings sharedStringsTable = xssfReader.getSharedStringsTable();

			// SAX 파서를 사용하여 시트 데이터를 처리
			Map<String, Object> sheetParser = fetchSheetParser(sharedStringsTable, styles);
			XMLReader xmlReader = (XMLReader) sheetParser.get("xmlReader");
			ExcelSheetHandler sheetHandler = (ExcelSheetHandler) sheetParser.get("sheetHandler");
			// 모든 시트를 순차적으로 처리
			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			while (sheets.hasNext()) {
				InputStream sheet = sheets.next();
				InputSource sheetSource = new InputSource(sheet);
				xmlReader.parse(sheetSource);
				for(LocationEntity entity : sheetHandler.getInsertRow()) {
					cntMap.put(
							"locationSize",
							cntMap.getOrDefault("locationSize", 0) + 1
					);
					Map<String, Object> locationMap = locationDAO.saveExcelLocationCode(entity);
					int location_id = Integer.parseInt(locationMap.get("id").toString());
					String location_status = locationMap.get("status").toString();
					if(location_status.equals("INSERT")) {
						cntMap.put(
								"saveLocNum",
								cntMap.getOrDefault("saveLocNum", 0) + 1
						);
					} else if(location_status.equals("SELECT")) {
						cntMap.put(
								"overlapLocNum",
								cntMap.getOrDefault("overlapLocNum", 0) + 1
						);
					}
				}

				for(DocumentEntity entity : sheetHandler.getDocumentRow()) {
					cntMap.put(
							"documentSize",
							cntMap.getOrDefault("documentSize", 0) + 1
					);
//					Map<String, Object> documentMap = documentDAO.saveDocument(entity);
					Map<String, Object> documentMap = null;
					int document_id = Integer.parseInt(documentMap.get("id").toString());
					String document_status = documentMap.get("status").toString();
					if(document_status.equals("INSERT")) {
						cntMap.put(
								"saveDocNum",
								cntMap.getOrDefault("saveDocNum", 0) + 1
						);
					} else if(document_status.equals("SELECT")) {
						cntMap.put(
								"overlapDocNum",
								cntMap.getOrDefault("overlapDocNum", 0) + 1
						);
					}
				}
				sheet.close();
			}

			resultDTO.setMessage(cntMap.getOrDefault("locationSize", 0) + "건의 코드정보 중 " +
					cntMap.getOrDefault("saveLocNum", 0) + "건의 코드정보 등록, " +
					cntMap.getOrDefault("overlapLocNum", 0) + "건의 중복코드 정보, " +
					cntMap.getOrDefault("documentSize", 0) + "건의 문서정보 중 " +
					cntMap.getOrDefault("saveDocNum", 0) + "건의 문서정보 등록, " +
					cntMap.getOrDefault("overlapDocNum", 0) + "건의 중복문서 정보, " +
					cntMap.getOrDefault("saveFacNum", 0) + "건의 입고처리 등록, " +
					cntMap.getOrDefault("overlapFacNum", 0) + "건의 중복입고 정보");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO saveLibraryList(List<Map<String, Object>> insertList) {
		resultDTO = new ResultDTO();
		Map<String, Integer> cntMap = new HashMap<>();

		try {
			for(Map<String, Object> map : insertList) {
				Factory factory = new Factory();

				// 위치정보 등록
				List<LocationEntity> locationEntities = ExcelSheetHandler.setInsertLocationEntity(map, null);
				for(LocationEntity entity : locationEntities) {
					cntMap.put(
							"locationSize",
							cntMap.getOrDefault("locationSize", 0) + 1
					);
					Map<String, Object> locationMap = locationDAO.saveExcelLocationCode(entity);
					int location_id = Integer.parseInt(locationMap.get("id").toString());
					String location_status = locationMap.get("status").toString();
					if(location_status.equals("INSERT")) {
						cntMap.put(
								"saveLocNum",
								cntMap.getOrDefault("saveLocNum", 0) + 1
						);
					} else if(location_status.equals("SELECT")) {
						cntMap.put(
								"overlapLocNum",
								cntMap.getOrDefault("overlapLocNum", 0) + 1
						);
					}
					factory.setLocation_id(location_id);
				}

				// 문서정보 등록
				List<DocumentEntity> documentEntities = ExcelSheetHandler.setInsertDocumentEntity(map, null);
				for(DocumentEntity entity : documentEntities) {
					cntMap.put(
							"documentSize",
							cntMap.getOrDefault("documentSize", 0) + 1
					);
//					Map<String, Object> documentMap = documentDAO.saveDocument(entity);
					Map<String, Object> documentMap = null;
					int document_id = Integer.parseInt(documentMap.get("id").toString());
					String document_status = documentMap.get("status").toString();
					if(document_status.equals("INSERT")) {
						cntMap.put(
								"saveDocNum",
								cntMap.getOrDefault("saveDocNum", 0) + 1
						);
					} else if(document_status.equals("SELECT")) {
						cntMap.put(
								"overlapDocNum",
								cntMap.getOrDefault("overlapDocNum", 0) + 1
						);
					}
					factory.setDocument_id(document_id);
				}

				FactoryEntity factoryEntity = new FactoryEntity(factory);
				int factoryChkNum = 0;
				if(factoryEntity.getDocument_id() > 0 && factoryEntity.getLocation_id() > 0) {
					factoryChkNum = factoryDAO.saveLocationDocument(factoryEntity);
					if (factoryChkNum > 0) {
						cntMap.put(
								"saveFacNum",
								cntMap.getOrDefault("saveFacNum", 0) + 1
						);
					} else {
						cntMap.put(
								"overlapFacNum",
								cntMap.getOrDefault("overlapFacNum", 0) + 1
						);
					}
				}
			}

			resultDTO.setState(true);
//			resultDTO.setResult("실패/성공 데이터들이 담길거다");
			resultDTO.setMessage(cntMap.getOrDefault("locationSize", 0) + "건의 코드정보 중 " +
					cntMap.getOrDefault("saveLocNum", 0) + "건의 코드정보 등록, " +
					cntMap.getOrDefault("overlapLocNum", 0) + "건의 중복코드 정보, " +
					cntMap.getOrDefault("documentSize", 0) + "건의 문서정보 중 " +
					cntMap.getOrDefault("saveDocNum", 0) + "건의 문서정보 등록, " +
					cntMap.getOrDefault("overlapDocNum", 0) + "건의 중복문서 정보, " +
					cntMap.getOrDefault("saveFacNum", 0) + "건의 입고처리 등록, " +
					cntMap.getOrDefault("overlapFacNum", 0) + "건의 중복입고 정보");
		} catch (Exception e) {
			resultDTO.setState(false);
		}

		return resultDTO;
	}


	@Override
	public ResultDTO saveLibrary(List<Map<String, Object>> libraryList, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();

		try {
			for(Map<String, Object> library : libraryList) {


				 String level1 = library.get("group_id") + "" +library.get("location_name_1");
				int group_id = (int)library.get("group_id");
				LocationDTO location = locationDAO.findLocationCodeByCode(level1);
				if(location == null){
					Location saveLocation = new Location();
					saveLocation.setLocation_code(level1);
					saveLocation.setShort_name(library.get("location_name_1").toString());
					saveLocation.setLocation_name(library.get("location_name_1").toString());
					saveLocation.setParent_code(library.get("group_id").toString());
					saveLocation.setSys_reg_user_id(auth.getName());
					saveLocation.setGroup_id(group_id);
					saveLocation.setDepth_level(1);
					saveLocation.setUse_flag('Y');
					locationDAO.saveLocationCode(saveLocation);
				}
				String level2 = level1 + "" +library.get("location_name_2");

				location = locationDAO.findLocationCodeByCode(level2);
				if(location == null){
					Location saveLocation = new Location();
					saveLocation.setLocation_code(level2);

					saveLocation.setShort_name(library.get("location_name_2").toString());
					saveLocation.setLocation_name(library.get("location_name_2").toString());
					saveLocation.setParent_code(level1);
					saveLocation.setSys_reg_user_id(auth.getName());
					saveLocation.setGroup_id(group_id);
					saveLocation.setDepth_level(2);
					saveLocation.setUse_flag('Y');
					locationDAO.saveLocationCode(saveLocation);
				}
				String level3 = level2 + "" +library.get("location_name_3");
				Factory factory = new Factory();
				location = locationDAO.findLocationCodeByCode(level3);
				if(location == null){
					Location saveLocation = new Location();
					saveLocation.setLocation_code(level3);

					saveLocation.setShort_name(library.get("location_name_3").toString());
					saveLocation.setLocation_name(library.get("location_name_3").toString());
					saveLocation.setParent_code(level2);
					saveLocation.setSys_reg_user_id(auth.getName());
					saveLocation.setGroup_id(group_id);
					saveLocation.setDepth_level(3);
					saveLocation.setUse_flag('Y');
					locationDAO.saveLocationCode(saveLocation);
					factory.setLocation_id(saveLocation.getId());
				}


				String level4 = level3 + "" +library.get("location_name_4");
				location = locationDAO.findLocationCodeByCode(level4);
				if(location == null){
					Location saveLocation = new Location();
					saveLocation.setLocation_code(level4);

					saveLocation.setShort_name(library.get("location_name_4").toString());
					saveLocation.setLocation_name(library.get("location_name_4").toString());
					saveLocation.setParent_code(level3);
					saveLocation.setSys_reg_user_id(auth.getName());
					saveLocation.setGroup_id(group_id);
					saveLocation.setDepth_level(4);
					saveLocation.setUse_flag('Y');
					locationDAO.saveLocationCode(saveLocation);

					// if(library.get("location_name_6") == null) {
					factory.setLocation_id(saveLocation.getId());
					// }
				}

				String level6 = "";

				if(library.get("location_name_6") != null) {
					level6 = library.get("location_name_6").toString();
				}
				String level5 = level4 + "" +library.get("location_name_5").toString()+""+level6;
				location = locationDAO.findLocationCodeByCode(level5);
				if(location == null){
					Location saveLocation = new Location();
					saveLocation.setLocation_code(level5);

					saveLocation.setShort_name(library.get("location_name_5")+""+level6);
					saveLocation.setLocation_name(library.get("location_name_5")+""+level6);
					saveLocation.setParent_code(level4);
					saveLocation.setSys_reg_user_id(auth.getName());
					saveLocation.setGroup_id(group_id);
					saveLocation.setDepth_level(5);
					saveLocation.setUse_flag('Y');
					locationDAO.saveLocationCode(saveLocation);

				   // if(library.get("location_name_6") == null) {
					factory.setLocation_id(saveLocation.getId());
				   // }
				}


//				if(library.get("location_name_6") != null) {
//					String level6 = level5 + "" + library.get("location_name_6");
//					location = locationDAO.findLocationCodeByCode(level6);
//					Location saveLocation = new Location();
//					if (location == null) {
//
//						saveLocation.setLocation_code(level6);
//
//						saveLocation.setShort_name(library.get("location_name_6").toString());
//						saveLocation.setLocation_name(library.get("location_name_6").toString());
//						saveLocation.setParent_code(level5);
//						saveLocation.setSys_reg_user_id(auth.getName());
//						saveLocation.setGroup_id(group_id);
//						saveLocation.setDepth_level(6);
//						saveLocation.setUse_flag('Y');
//						locationDAO.saveLocationCode(saveLocation);
//					}
//					factory.setLocation_id(saveLocation.getId());
//
//				}



				level1 = group_id + "" +library.get("doc_num_1");
				DocCodeDTO docCode = docCodeDAO.findDocCode(level1);
				if(docCode == null){
					DocCode docCode1 = new DocCode();
					docCode1.setDoc_code(level1);

					docCode1.setShort_name(library.get("doc_num_1").toString());
					docCode1.setDoc_code_name(library.get("doc_num_1").toString());
					docCode1.setParent_code(library.get("group_id").toString());
					docCode1.setSys_reg_user_id(auth.getName());
					docCode1.setGroup_id(group_id);
					docCode1.setDepth_level(1);
					docCode1.setUse_flag('Y');
					docCodeDAO.saveDocCode(docCode1);
				}
				level2 = level1 + "" +library.get("doc_num_2");
				docCode = docCodeDAO.findDocCode(level2);
				if(docCode == null){
					DocCode docCode1 = new DocCode();
					docCode1.setDoc_code(level2);
					docCode1.setShort_name(library.get("doc_num_2").toString());
					docCode1.setDoc_code_name(library.get("doc_num_2").toString());
					docCode1.setParent_code(level1);
					docCode1.setSys_reg_user_id(auth.getName());
					docCode1.setGroup_id(group_id);
					docCode1.setDepth_level(2);
					docCode1.setUse_flag('Y');
					docCodeDAO.saveDocCode(docCode1);
				}
				level3 = level2 + "" +library.get("doc_num_3");
				docCode = docCodeDAO.findDocCode(level3);
				if(docCode == null){
					DocCode docCode1 = new DocCode();
					docCode1.setDoc_code(level3);
					docCode1.setShort_name(library.get("doc_num_3").toString());
					docCode1.setDoc_code_name(library.get("doc_num_3").toString());
					docCode1.setParent_code(level2);
					docCode1.setSys_reg_user_id(auth.getName());
					docCode1.setGroup_id(group_id);
					docCode1.setDepth_level(3);
					docCode1.setUse_flag('Y');
					docCodeDAO.saveDocCode(docCode1);
				}

				level4 = level3 + "" +library.get("doc_num_4");
				docCode = docCodeDAO.findDocCode(level4);
				if(docCode == null){
					DocCode docCode1 = new DocCode();
					docCode1.setDoc_code(level4);
					docCode1.setShort_name(library.get("doc_num_4").toString());
					docCode1.setDoc_code_name(library.get("doc_num_4").toString());
					docCode1.setParent_code(level3);
					docCode1.setSys_reg_user_id(auth.getName());
					docCode1.setGroup_id(group_id);
					docCode1.setDepth_level(4);
					docCode1.setUse_flag('Y');
					docCodeDAO.saveDocCode(docCode1);
				}

				level5 = level4 + "" +library.get("doc_num_5");
				docCode = docCodeDAO.findDocCode(level5);
				if(docCode == null){
					DocCode docCode1 = new DocCode();
					docCode1.setDoc_code(level5);
					docCode1.setShort_name(library.get("doc_num_5").toString());
					docCode1.setDoc_code_name(library.get("doc_num_5").toString());
					docCode1.setParent_code(level4);
					docCode1.setSys_reg_user_id(auth.getName());
					docCode1.setGroup_id(group_id);
					docCode1.setDepth_level(5);
					docCode1.setUse_flag('Y');
					docCodeDAO.saveDocCode(docCode1);
				}


				// 문서정보 등록
				DocumentEntity dentity = new DocumentEntity();
					dentity.setDept_cd(user.getDept_cd());
					dentity.setDept_nm(user.getDept_nm());
					dentity.setReg_user_id(auth.getName());
					dentity.setDocument_num(library.get("document_num").toString());
					dentity.setGroup_id(group_id);
					dentity.setDocument_cnt("1");
					dentity.setBinder(3);

					dentity.setTitle(library.get("title").toString());
					dentity.setWrite_user_id(auth.getName());
					dentity.setDoc_code(level5);

					int docResultId = documentDAO.saveDocument(dentity);
					int document_id = 0;
					if(docResultId > 0) {
						document_id = dentity.getId();
					}
					DocumentMemo documentMemo = new DocumentMemo();
					documentMemo.setDocument_id(document_id);
					documentMemo.setMemo("엑셀 업로드");
					documentMemo.setSys_reg_user_id(auth.getName());
					documentMemo.setType("doc");
					documentDAO.saveDocumentMemo(documentMemo);
					factory.setDocument_id(document_id);


				// 문서위치 등록
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String display_date = library.get("display_date") != null ?
						String.valueOf(library.get("display_date")) : null;
				String display_user_id = library.get("display_user_id") != null ?
						String.valueOf(library.get("display_user_id")) : null;
				if(!DataLib.isEmpty(display_date)) {
					factory.setDisplay_date(display_date);
				}
				if(!DataLib.isEmpty(display_user_id)) {
					factory.setDisplay_user_id(display_user_id);
				}
				FactoryEntity factoryEntity = new FactoryEntity(factory);
				int factoryChkNum = 0;
				if(factoryEntity.getDocument_id() > 0 && factoryEntity.getLocation_id() > 0) {
					factoryEntity.setSys_reg_user_id(auth.getName());
					factoryChkNum = factoryDAO.saveLocationDocument(factoryEntity);
				}

				// 코드 목록 조회 docstatus
				Code findCode = new Code();
				findCode.setCode_name("docstatus");
				List<CodeDTO> codeList = codeDAO.findCodeList(findCode);
				String libraryStatus = library.get("status") != null ? library.get("status").toString() : StringUtils.EMPTY;
				String documentStatus = null;
				for(CodeDTO code : codeList) {
					if(code.getDiscription().indexOf(libraryStatus) >= 0) {
						documentStatus = code.getCode_name();
						break;
					}
				}

				// 문서상태 등록
				DocumentLoanEntity documentLoanEntity = new DocumentLoanEntity(
						factory.getDocument_id(),
						"docs11003"
				);
				documentLoanEntity.setSys_reg_user_id(auth.getName());
				documentDAO.saveDocumentLoan(documentLoanEntity);
			}

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"엑셀 문서등록"}, Locale.KOREA));
			resultDTO.setResult(null);
		} catch (Exception e) {

			System.out.println(e.getCause().toString());
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"엑셀 문서등록"}, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO deleteTrashDocument() {
		resultDTO = new ResultDTO();
		int deleteNum = documentDAO.deleteTrashDocument();
		if(deleteNum > 0) {
			resultDTO.setMessage("개발과정 TB_DOCUMENT 데이터 삭제 완료");
		} else {
			resultDTO.setMessage("TB_DOCUMENT 데이터 삭제 실패");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO deleteAllTrashDocument() {
		resultDTO = new ResultDTO();
		int deleteDocNum = documentDAO.deleteTrashDocument();
		int deleteLocNum = locationDAO.deleteTrashLocation();
		int deleteFacNum = factoryDAO.deleteTrashLocationDocument();
		int totalDeleteNum = deleteDocNum + deleteLocNum + deleteFacNum;
		if(totalDeleteNum > 0) {
			resultDTO.setMessage(deleteDocNum + "건의 TB_DOCUMENT 데이터 삭제 완료, " +
					deleteLocNum + "건의 TB_LOCATION 데이터 삭제 완료, " +
					deleteFacNum + "건의 TB_LOCATION_DOCUMENT 데이터 삭제 완료.");
		} else {
			resultDTO.setMessage("삭제된 데이터가 없습니다");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findApproveMyworkList(Document document, Authentication auth) {
		resultDTO = new ResultDTO();
		//todo 정주원 롤
		User user = (User) auth.getPrincipal();

		Collection<Role> roles = user.getRoles();
		for(Role role : roles) {
			List<ApproveMyworkDTO> resultList = documentDAO.findApproveMyworkList(role.getId());
			if(!DataLib.isEmpty(resultList)) {
				resultDTO.setState(true);
				resultDTO.setResult(resultList);
			} else {
				resultDTO.setState(false);
			}
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findDocumentLoanList(Document document, Authentication auth) {
		resultDTO = new ResultDTO();
		DocumentSearchEntity entity = new DocumentSearchEntity(document);

		User user = (User) auth.getPrincipal();
		boolean chkUserRole = auth.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		if(!chkUserRole) {
			entity.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
		}

		int listSize = documentDAO.findDocumentLoanListCnt(entity);
		if(document.getPage() != null) {
			entity.setPage(document.getPage());
		}

		List<DocumentDTO> resultList = documentDAO.findDocumentLoanList(entity);
		if(document.getPage() != null) {
			resultDTO.setPage(document.getPage());
			resultDTO.getPage().setTotalCnt(resultList);
		}

		if(!DataLib.isEmpty(resultList)) {
			resultDTO.setState(true);
			resultDTO.setResult(resultList);
			resultDTO.setMessage("total Size : " + listSize);
		} else {
			resultDTO.setState(false);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findDocument(Document document, Authentication auth) {
		resultDTO = new ResultDTO();
		DocumentDTO re_document = documentDAO.findDocument( document);
		String document_num = re_document.getDocument_num();
		String documentNumPlus = document.getDocument_num();
		if(document_num != null) {
			int docLastNum = Integer.parseInt(document_num.substring(document_num.lastIndexOf("-") + 1,document_num.length())) + 1;
			documentNumPlus += String.valueOf("-" + docLastNum);

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서 번호 생성"}, Locale.KOREA));
			resultDTO.setResult(documentNumPlus);
		} else {
			documentNumPlus = documentNumPlus +"-1";
			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서 번호 생성"}, Locale.KOREA));
			resultDTO.setResult(documentNumPlus);
		}
		return resultDTO;
	}
	@Transactional
	@Override
	public ResultDTO saveDocumentForm(DocumentMemo memo, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		resultDTO = commonService.findCheckUserAuth(memo.getUser(), request, auth);
		if(resultDTO.getCode() == 200) {
			User user = (User) auth.getPrincipal();
			memo.setSys_reg_user_id(auth.getName());

			// 결재유형코드(sign_category) = 문서(comc11002) and 상태(state) = 요청(coms11004) 건이 있는지 체크
			int signCnt = signDAO.findPlanSignState(memo.getDocument_id());
			if (signCnt > 0) {
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage("이미 결재가 진행중인 문서입니다.");
				resultDTO.setResult(null);

				return resultDTO;
			}

			try {
				DocumentApplyEntity applyEntity = new DocumentApplyEntity();
				applyEntity.setReq_user_id(auth.getName());
				applyEntity.setDocument_id(memo.getDocument_id());
				// 기존코드 : parent_code='comi2' , 바뀌는 코드 : parent_code='docs1' : 이수철
				if(memo.getSignform().equals("comi2s1")) {		  // 대출신청의 경우
					applyEntity.setStatus("docs11006");			 // 문서상태 : 대출신청코드로 변경
				} else if(memo.getSignform().equals("comi2s2")) {   // 폐기신청의 경우
					applyEntity.setStatus("docs11008");			 // 문서상태 폐기신청코드로 변경
				} else if(memo.getSignform().equals("comi2s3")) {   // 연장신청의 경우
					applyEntity.setStatus("docs11007");			 // 문서상태 : 연장신청코드로 변경
				} else {
					applyEntity.setStatus(memo.getStatus());
				}
				applyEntity.setCurrent_start_date(memo.getCurrent_start_date());
				applyEntity.setCurrent_end_date(memo.getCurrent_end_date());
				applyEntity.setSys_reg_user_id(auth.getName());
				applyEntity.setUpdate_type("set");
				
				//아래 체크 필요 : 이수철
				int cnt = factoryDAO.updateDocumentLoan(applyEntity);
				if (cnt > 0) {
					PlanSign planSign = new PlanSign();
					planSign.setId(memo.getSign_id());
					//문서 인경우 plan_id 에 문서ID 입력
					planSign.setPlan_id(memo.getDocument_id());
					planSign.setStatus(memo.getSignUserList().get(1).getSign_type());
					planSign.setSys_reg_user_id(auth.getName());
					planSign.setDept_cd(user.getDept_cd());
					signDAO.savePlanSign(planSign);
					
					memo.setType("doc");
					int resultId = documentDAO.saveDocumentMemo(memo);
					if (resultId > 0) {
						int order_num = 1;
						for (PlanSignManager planSignManager : memo.getSignUserList()) {
							int singId = planSignManager.getSign_id() == 0 ? memo.getSign_id() : planSignManager.getSign_id();
							planSignManager.setSign_id(singId);
							planSignManager.setPlan_id(memo.getDocument_id());
							planSignManager.setSign_type(planSignManager.getSign_type());//작성,검토,승인
							planSignManager.setOrder_num(order_num);
							planSignManager.setSign_category("comc11002");//문서
							planSignManager.setGroup_id(memo.getGroup_id());//공장 <- 메모에 그룹ID 다시 체크 : 이수철
							planSignManager.setSys_reg_user_id(auth.getName());
							planSignManager.setDept_cd(user.getDept_cd());
							planSignManager.setPlan_sign_id(planSign.getId());//유형별결재ID
							planSignManager.setComment(memo.getMemo());

							if (order_num == 1) {
								planSignManager.setState("coms11005");//승인
								planSignManager.setUser_id(auth.getName());
							}
							else if (order_num == 2) {
								planSignManager.setState("coms11004");//요청
							}
							else {
								planSignManager.setState("coms11007");//대기
							}
							// tb_plan_sign_manager 입력
							signDAO.savePlanSignManager(planSignManager);
							/*
							if (order_num == 1) {
								PlanSignDetail planSignDetail = new PlanSignDetail();
								planSignDetail.setPlan_sign_id(planSign.getId());
								planSignDetail.setSign_manager_id(planSignManager.getId());
								planSignDetail.setState("coms11005");//승인
								planSignDetail.setUser_id(auth.getName());
								planSignDetail.setSys_reg_user_id(auth.getName());
								// TB_PLAN_SIGN_DETAIL
								signDAO.savePlanSignDetail(planSignDetail);
							}
							*/
							order_num++;
						}

						int logId = commonService.saveLog(LogEntity.builder()
								.type("doc")
								.table_id(memo.getId())
								.page_nm("approve")
								.url_addr(request.getRequestURI())
								.state("insert")
								.reg_user_id(auth.getName())
								.build());
						CodeDTO re_code = codeDAO.findByCodeName(memo.getSignUserList().get(1).getSign_type());

						commonService.saveLogDetail(LogChild
								.builder()
								.log_id(logId)
								.field("status")
								.new_value(re_code.getCode_name())
								.reg_user_id(auth.getName())
								.build());
						commonService.saveLogDetail(LogChild
								.builder()
								.log_id(logId)
								.field("sign")
								.new_value(String.valueOf(planSign.getId()))
								.reg_user_id(auth.getName())
								.build());
						if ("".equals(memo.getSignUserList().get(1).getSign_type())) {

							commonService.saveLogDetail(LogChild
									.builder()
									.log_id(logId)
									.field("period")
									.new_value(memo.getCurrent_start_date() + "~" + memo.getCurrent_end_date())
									.reg_user_id(auth.getName())
									.build());

						}

						resultDTO.setState(true);
						resultDTO.setCode(201);
						resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서신청"}, Locale.KOREA));
						resultDTO.setResult(memo.getId());
					}
				} else {
					resultDTO.setState(false);
					resultDTO.setCode(400);
					resultDTO.setMessage("입고되지 않은 문서입니다.");
					resultDTO.setResult(null);
				}
			} catch (Exception e) {
				resultDTO.setState(false);
				resultDTO.setCode(400);
				resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"문서신청"}, Locale.KOREA));
				resultDTO.setResult(memo.getId());
			}
			return resultDTO;
		}
		return resultDTO;
	}

	@Override
	public ResultDTO findDocumentMemoList(DocumentMemo memo, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		try {

			DocumentMemo param = new DocumentMemo();
			param.setDocumentMemoIds(memo.getDocumentMemoIds());
			param.setType(memo.getType());
			List<DocumentMemo> resultList = documentDAO.findDocumentMemoList(param);

			if(!DataLib.isEmpty(resultList)) {
				for (DocumentMemo documentMemo : resultList) {
						List<LogDTO> documentList = commonDAO.findLogList(LogEntity.builder()
								.type(memo.getType())
								.table_id(documentMemo.getId())
								.build());
						documentMemo.setDocumentLog(documentList);
				}
				resultDTO.setState(true);
				resultDTO.setCode(200);
				resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[]{"Audit Trail"}, Locale.KOREA));
				resultDTO.setResult(resultList);
			}
		} catch(Exception e) {
			System.out.println(e.getCause().toString());
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"Audit Trail"}, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO updateDocumentLoan(Document document, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();
		
		//문서번호(plan_id) 기준 sign_category=문서 이고 state=요청 인 경우 카운트
		int signCnt = signDAO.findPlanSignState(document.getId());
		// docs11009 = 대출승인
		if(signCnt > 0 && !document.getStatus().equals("docs11009")) {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage("이미 결재가 진행중인 문서입니다.");
			resultDTO.setResult(null);

			return resultDTO;
		}

		try {
			String before_state = "";
			//대출신청 || 대출승인
			if(document.getStatus().equals("docs11006") || document.getStatus().equals("docs11009")) {
				// 반납 docs11006 ==> 입고 docs11003
				// 대출승인 docs11009 ==> 대출 docs11001
				DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
				docApplyEntity.setStatus(document.getStatus());
				docApplyEntity.setSys_reg_user_id(auth.getName());
				docApplyEntity.setDocument_id(document.getId());
				factoryDAO.updateDocumentLoan(docApplyEntity);
			}
			//연장신청
			else if (document.getStatus().equals("docs11007")) {
				// 연장 docs11007 ==> 대출 docs11001
				DocumentApplyEntity docApplyEntity = new DocumentApplyEntity();
				docApplyEntity.setStatus(document.getStatus());
				//docApplyEntity.setCurrent_end_date(document.getCurrent_end_date());
				docApplyEntity.setCurrent_end_date_req(document.getCurrent_end_date());//대출연장요청 연장기한
				docApplyEntity.setReq_user_id(auth.getName());
				docApplyEntity.setSys_reg_user_id(auth.getName());
				docApplyEntity.setDocument_id(document.getId());
				docApplyEntity.setUpdate_type("set");
				int updateLoanCnt = factoryDAO.updateDocumentLoan(docApplyEntity);

				// 결재정보 조회 후 연장신청으로 동일한 결재정보 등록
				PlanSignManager planSignManager = new PlanSignManager();
				planSignManager.setSign_category("comc11002"); // 문서
				planSignManager.setSignform("comi2s3"); // 연장신청
				planSignManager.setGroup_id(document.getGroup_id());

				// 연장신청 결재선 정보를 가져 온다.
				List<PlanSignManager> planSignManagerList = signDAO.findPlanSignManagerExtensionRequest(planSignManager);

				if (updateLoanCnt > 0) {
					PlanSign planSign = new PlanSign();
					planSign.setPlan_id(document.getId());
					planSign.setStatus(planSignManagerList.get(1).getSign_type());
					planSign.setSignform("comi2s3");
					planSign.setSys_reg_user_id(auth.getName());
					planSign.setDept_cd(user.getDept_cd());
					planSign.setSign_category_type(planSignManagerList.get(1).getSign_category());
					planSign.setId(planSignManagerList.get(1).getSign_id());
					int cnt = signDAO.savePlanSign(planSign);

					if (cnt > 0) {
						int order_num = 1;
						for (PlanSignManager psm : planSignManagerList) {
							psm.setPlan_sign_id(planSign.getId());
							psm.setPlan_id(document.getId());
							psm.setSign_category("comc11002");//문서
							psm.setSys_reg_user_id(auth.getName());
							psm.setDept_cd(user.getDept_cd());
							psm.setOrder_num(order_num);
							psm.setGroup_id(document.getGroup_id());//이수철 체크 : 값이 있는지 체크
							
							if (order_num == 1) {
								psm.setState("coms11005");//승인
								psm.setComment(document.getMemo());
								psm.setUser_id(auth.getName());
							}
							else if (order_num == 2) {
								psm.setState("coms11004");//요청
							}
							else {
								psm.setState("coms11007");//대기
							}
							signDAO.savePlanSignManager(psm);
							/*
							if (order_num == 1) {
								PlanSignDetail planSignDetail = new PlanSignDetail();
								planSignDetail.setPlan_sign_id(planSign.getId());
								planSignDetail.setSign_manager_id(signUser.getId());
								planSignDetail.setComment(document.getMemo());
								planSignDetail.setState("coms11005");
								planSignDetail.setUser_id(auth.getName());
								planSignDetail.setSys_reg_user_id(auth.getName());
								planSignDetail.setComment(document.getMemo());
								signDAO.savePlanSignDetail(planSignDetail);
							}
							*/
							order_num++;
						}
					}
				}
			}

			// 문서 비고 등록
			DocumentMemo memo = new DocumentMemo();
			memo.setDocument_id(document.getId());
			memo.setMemo(document.getMemo());
			memo.setSys_reg_user_id(auth.getName());
			memo.setType("doc");
			documentDAO.saveDocumentMemo(memo);

			commonService.saveLog(LogEntity.builder()
				.type("doc")
				.table_id(memo.getId())
				.page_nm("documentLoan")
				.url_addr(request.getRequestURI())
				.state("update")
				.reg_user_id(auth.getName())
				.build());

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"대출관리"}, Locale.KOREA));
			resultDTO.setResult(memo.getId());
		}
		catch(Exception e) {
			//to do : DB 롤백 넣어야 함 : 이수철
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[]{"대출관리"}, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findDocumentNo(Document document, Authentication auth) {
		resultDTO = new ResultDTO();
		DocumentDTO re_document = documentDAO.findDocumentNo( document);
		String document_num = re_document == null ? document.getDocument_num()+"-0" : re_document.getDocument_num();
		String documentNumPlus = document.getDocument_num();
		if(document_num != null) {
			int docLastNum = Integer.parseInt(document_num.substring(document_num.lastIndexOf("-") + 1,document_num.length())) + 1;
			String formatLastNum = String.format("%04d", docLastNum);
			documentNumPlus += "-" + formatLastNum;

			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서 번호 생성"}, Locale.KOREA));
			resultDTO.setResult(documentNumPlus);
		} else {
			documentNumPlus = documentNumPlus +"-0001";
			resultDTO.setState(true);
			resultDTO.setCode(201);
			resultDTO.setMessage(messageSource.getMessage("api.message.201", new String[]{"문서 번호 생성"}, Locale.KOREA));
			resultDTO.setResult(documentNumPlus);
		}
		return resultDTO;
	}

	// SAX 파서 생성 및 설정
	private static Map<String, Object> fetchSheetParser(SharedStrings sharedStringsTable, StylesTable styles) {
		Map<String, Object> returnMap = new HashMap<>();
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setNamespaceAware(true);
		XMLReader xmlReader;
		try {
			SAXParser parser = saxParserFactory.newSAXParser();
			xmlReader = parser.getXMLReader();
			XSSFSheetXMLHandler.SheetContentsHandler sheetHandler = new ExcelSheetHandler();
			XSSFSheetXMLHandler xssfSheetXMLHandler = new XSSFSheetXMLHandler(styles, sharedStringsTable, sheetHandler, false);
			xmlReader.setContentHandler(xssfSheetXMLHandler);
			returnMap.put("xmlReader", xmlReader);
			returnMap.put("sheetHandler", sheetHandler);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}

		return returnMap;
	}
}
