package com.innon.education.admin.mypage.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.innon.education.admin.mypage.dao.MypageDAO;
import com.innon.education.admin.mypage.repository.dto.MyCurrentDTO;
import com.innon.education.admin.mypage.repository.dto.SignCurrentDTO;
import com.innon.education.admin.mypage.repository.entity.MypageEntity;
import com.innon.education.admin.mypage.repository.model.Mypage;
import com.innon.education.admin.mypage.service.MypageService;
import com.innon.education.auth.entity.User;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.service.CommonService;
import com.innon.education.common.util.DataLib;
import com.innon.education.code.controller.dto.ResultDTO;
import com.innon.education.code.controller.dao.CodeDAO;
import com.innon.education.educurrent.dao.EduCurrentDAO;
import com.innon.education.library.document.dao.DocumentDAO;
import com.innon.education.library.document.repasitory.dto.DocumentDTO;
import com.innon.education.library.document.repasitory.entity.DocumentSearchEntity;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MypageServiceImpl implements MypageService {

	private ResultDTO resultDTO;
	@Autowired
	MypageDAO mypageDAO;
	@Autowired
	EduCurrentDAO eduCurrentDAO;
	@Autowired
	DocumentDAO documentDAO;
	@Autowired
	MessageSource messageSource;
	@Autowired
	CommonService commonService;
	@Autowired
	CodeDAO codeDAO;
	@Autowired
	CommonDAO commonDAO;

	@Override
	public ResultDTO updateEduInsaInfo(Mypage mypage) {
		resultDTO = new ResultDTO();
		MypageEntity entity = new MypageEntity(mypage);
		int updateNum = mypageDAO.updateEduInsaInfo(entity);
		if (updateNum > 0) {
			resultDTO.setMessage("인사 정보 수정이 성공하였습니다.");
		}
		else {
			resultDTO.setMessage("인사 정보 수정이 실패하였습니다.");
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findMyCurrentInfo(Mypage mypage, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		MyCurrentDTO myCurrentDTO = new MyCurrentDTO();
		DocumentSearchEntity docEntity = new DocumentSearchEntity();
		docEntity.setReq_user_id(mypage.getUser_id());
		docEntity.setSearch_txt(mypage.getSearch_txt());
		docEntity.setSearch_type(mypage.getSearch_type());
		docEntity.setReq_date_end(mypage.getReq_date_end());
		docEntity.setReq_date_start(mypage.getReq_date_start());
		docEntity.setView_nm("mypage");

		try {
			User user = (User) auth.getPrincipal();
			boolean chkUserRole = auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
			if (!chkUserRole) {
				mypage.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
			} ;

			mypage.setSign_category("comc11001");   // 교육
			myCurrentDTO.setSignEduCurrentCnt(mypageDAO.findSignCurrentEducationList(mypage).size());
			mypage.setSign_category("comc11002");   // 문서
			myCurrentDTO.setSignDocCurrentCnt(mypageDAO.findSignCurrentDocumentList(mypage).size());

			myCurrentDTO.setEduPlanCnt(eduCurrentDAO.findEduPlanByUserId(mypage.getUser_id()).size());
			myCurrentDTO.setEduPrgsCnt(eduCurrentDAO.findEduProgressByUserId(mypage.getUser_id()).size());
			List<ManagementPlanDTO> evalList = eduCurrentDAO.findEduEvaluationByUserId(mypage.getUser_id());
			Iterator<ManagementPlanDTO> iterator = evalList.iterator();
			while (iterator.hasNext()) {
				ManagementPlanDTO eval = iterator.next();
				if (eval.getUser_status().equals("edus21003") && eval.getRelation_num().equals(String.valueOf(eval.getAnswer_cnt())) && eval.getPass_yn() != 'Y') {
					iterator.remove();
				}
			}
			myCurrentDTO.setEduEvalCnt(evalList.size());
			myCurrentDTO.setEduResultCnt(eduCurrentDAO.findEduResultByUserId(mypage.getUser_id()).size());

			//            docEntity.setStatus("docs11003");   // 입고
			//            myCurrentDTO.setDocTransCnt(documentDAO.findDocumentLoanListCnt(docEntity));
			docEntity.setSignform("comi2s1");   // 대출신청
			myCurrentDTO.setDocSubCnt(documentDAO.findDocumentLoanListCnt(docEntity));
			docEntity.setSignform("comi2s2");   // 폐기신청
			myCurrentDTO.setDocReadCnt(documentDAO.findDocumentLoanListCnt(docEntity));
			docEntity.setSignform(null);
			docEntity.setStatus("docs11001");   // 대출중
			myCurrentDTO.setDocLoanCnt(documentDAO.findDocumentLoanListCnt(docEntity));

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "현황" }, Locale.KOREA));
			resultDTO.setResult(myCurrentDTO);
		}
		catch (Exception e) {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "현황" }, Locale.KOREA));
			resultDTO.setResult(myCurrentDTO);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findMySignInfo(Mypage mypage, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();

		mypage.setUser_id(auth.getName());
		boolean chkUserRole = auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		if (!chkUserRole) {
			mypage.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
		}

		List<SignCurrentDTO> signCurrentList;
		if (mypage.getSign_category().equals("comc11001")) {
			// 교육
			signCurrentList = mypageDAO.findSignCurrentEducationList(mypage);
		}
		else {
			// 문서 : "comc11002"
			signCurrentList = mypageDAO.findSignCurrentDocumentList(mypage);
		}

		if (!DataLib.isEmpty(signCurrentList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .page_nm("mypage")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재현황" }, Locale.KOREA));
			resultDTO.setResult(signCurrentList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재현황" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findMyEducationInfo(Mypage mypage, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		List<ManagementPlanDTO> eduCurrentList = new ArrayList<>();

		if (mypage.getSearch_type().equals("temp")) {

			List<ManagementPlanDTO> eduQmsList = eduCurrentDAO.findEduQmsByEduUserId(mypage.getUser_id());
			eduCurrentList.addAll(eduQmsList);

		}
		else {
			List<ManagementPlanDTO> eduPlanList = eduCurrentDAO.findEduPlanByUserId(mypage.getUser_id());
			List<ManagementPlanDTO> eduPrgsList = eduCurrentDAO.findEduProgressByUserId(mypage.getUser_id());
			List<ManagementPlanDTO> eduEvalList = eduCurrentDAO.findEduEvaluationByUserId(mypage.getUser_id());
			//            List<ManagementPlanDTO> eduResultList = eduCurrentDAO.findEduResultByUserId(mypage.getUser_id());
			eduCurrentList.addAll(eduPlanList);
			eduCurrentList.addAll(eduPrgsList);
			eduCurrentList.addAll(eduEvalList);
			//            eduCurrentList.addAll(eduResultList);

		}

		if (!DataLib.isEmpty(eduCurrentList)) {
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "교육현황" }, Locale.KOREA));
			resultDTO.setResult(eduCurrentList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "교육현황" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findMyDocumentInfo(Mypage mypage, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		DocumentSearchEntity docEntity = new DocumentSearchEntity();
		docEntity.setReg_user_id(mypage.getUser_id());
		docEntity.setSearch_txt(mypage.getSearch_txt());
		docEntity.setSearch_type(mypage.getSearch_type());
		docEntity.setReq_date_end(mypage.getReq_date_end());
		docEntity.setReq_date_start(mypage.getReq_date_start());
		docEntity.setView_nm("mypage");

		docEntity.setStatus("docs11008");   // 폐기신청
		List<DocumentDTO> docDisposeList = documentDAO.findDocumentLoanList(docEntity);
		docEntity.setStatus("docs11007");   // 연장신청
		List<DocumentDTO> docContractList = documentDAO.findDocumentLoanList(docEntity);
		docEntity.setStatus("docs11006");   // 대출신청
		List<DocumentDTO> docLoanList = documentDAO.findDocumentLoanList(docEntity);
		docEntity.setStatus("docs11003");   // 입고
		List<DocumentDTO> docViewList = documentDAO.findDocumentLoanList(docEntity);
		docEntity.setStatus("docs11004");   // 작성
		List<DocumentDTO> docWriteList = documentDAO.findDocumentLoanList(docEntity);
		docEntity.setStatus("docs11005");   // 만료
		List<DocumentDTO> docExpiredList = documentDAO.findDocumentLoanList(docEntity);
		docEntity.setStatus("docs11001");   // 대출중
		List<DocumentDTO> docLoaningList = documentDAO.findDocumentLoanList(docEntity);

		List<DocumentDTO> docCurrentList = new ArrayList<>();

		docCurrentList.addAll(docDisposeList);
		docCurrentList.addAll(docContractList);
		docCurrentList.addAll(docLoanList);
		docCurrentList.addAll(docViewList);
		docCurrentList.addAll(docWriteList);
		docCurrentList.addAll(docExpiredList);
		docCurrentList.addAll(docLoaningList);

		if (!DataLib.isEmpty(docCurrentList)) {
			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "문서현황" }, Locale.KOREA));
			resultDTO.setResult(docCurrentList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "문서현황" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}

	@Override
	public ResultDTO findMyRequestSignInfo(Mypage mypage, HttpServletRequest request, Authentication auth) {
		resultDTO = new ResultDTO();
		User user = (User) auth.getPrincipal();

		mypage.setReq_user_id(auth.getName());
		boolean chkUserRole = auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
		if (!chkUserRole) {
			mypage.setGroupList(commonDAO.findGroupInfoByDeptCd(user.getDept_cd()));
		} ;
		List<SignCurrentDTO> signCurrentList = mypageDAO.findMyRequestSignInfo(mypage);// 이수철 : 어디서 사용하는지 체크하고, 쿼리문 수정해야 함

		if (!DataLib.isEmpty(signCurrentList)) {
			//            int logId = commonService.saveLog(LogEntity.builder()
			//                    .page_nm("mypage")
			//                    .url_addr(request.getRequestURI())
			//                    .state("view")
			//                    .reg_user_id(auth.getName())
			//                    .build());

			resultDTO.setState(true);
			resultDTO.setCode(200);
			resultDTO.setMessage(messageSource.getMessage("api.message.200", new String[] { "결재현황" }, Locale.KOREA));
			resultDTO.setResult(signCurrentList);
		}
		else {
			resultDTO.setState(false);
			resultDTO.setCode(400);
			resultDTO.setMessage(messageSource.getMessage("api.message.400", new String[] { "결재현황" }, Locale.KOREA));
			resultDTO.setResult(null);
		}

		return resultDTO;
	}
}
