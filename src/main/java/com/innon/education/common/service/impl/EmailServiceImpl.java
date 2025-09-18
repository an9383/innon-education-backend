package com.innon.education.common.service.impl;

import com.innon.education.admin.system.sign.dao.SignDAO;
import com.innon.education.admin.system.sign.repository.PlanSign;
import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.annual.plan.dao.AnnualPlanDAO;
import com.innon.education.annual.plan.repository.dto.AnnualPlanDTO;
import com.innon.education.annual.plan.repository.entity.AnnualPlanSearchEntity;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.common.repository.dto.CodeDTO;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.common.service.EmailService;
import com.innon.education.code.controller.dao.CodeDAO;
import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.management.plan.dao.ManagementPlanDAO;
import com.innon.education.management.plan.repository.dto.ManagementPlanDTO;
import com.innon.education.management.plan.repository.model.ManagementPlan;
import com.innon.education.qualified.current.dao.CurrentDAO;
import com.innon.education.qualified.current.repository.model.JobRevision;

import io.micrometer.common.util.StringUtils;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
	@Autowired
	SignDAO signDAO;
	@Autowired
	CodeDAO codeDAO;

	@Autowired
	AnnualPlanDAO annualPlanDAO;
	@Autowired
	ManagementPlanDAO managementPlanDAO;

	@Autowired
	CurrentDAO currentDAO;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	UserRepository userRepository;
	
	@Value("${spring.mail.host}")
	String smtpServer;
	@Value("${spring.mail.email}")
	String smtpUser;
	@Value("${spring.mail.host}")
	String smtpPort;

	// 문자보내기 공통 모듈
	// signStatus : ing , last
	public String sendMail(String userId, PlanSignManager planSignManager, String signStatus, String returnUrl) {
		if (!StringUtils.isEmpty(userId)) {
			LoginRequest paramUser = new LoginRequest();
			paramUser.setUser_id(userId);

			CustomUserDetails customUserDetails = userRepository.findUser(paramUser);

			String emailContent = messageSignInfo(planSignManager, customUserDetails, signStatus);
			
			EmailMessage message = EmailMessage.builder()
				.to(customUserDetails.getEmail())
				.subject(createSubject(planSignManager, customUserDetails, signStatus))
				.url(returnUrl)
				.message(emailContent)
				.build();
			return sendMail(message, "email");
		}
		return null;
	}

	@Override
	public String sendMail(EmailMessage emailMessage, String type) {
		String authNum = createCode();

		// SMTP 인증방식을 이용하는 경우
		// String smtpUserid = "SMTP 인증 계정";
		// String smtpPwd = "SMTP 인증 비밀번호";

		Properties prop = new Properties();

		// 메일서버 주소
		prop.put("mail.smtp.host", smtpServer);
		prop.put("mail.smtp.user", smtpUser);
		// 포트(기본 25)
		prop.put("mail.smtp.port", smtpPort);

		// SMTP 인증 기능 사용시
		//prop.put("mail.smtp.auth", "true");

		// ssl 사용시(이때는 보통 port 465)
		prop.put("mail.smtp.ssl.enable", "false");
		// tls 사용시(ssl 을 사용할 경우에는 주석)
		//prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		prop.put("mail.smtp.ssl.checkserveridentity", "false");
		prop.put("mail.smtp.ssl.trust", "*");
		prop.put("mail.protocol.ssl.trust", "*");

		Session session = Session.getDefaultInstance(prop, null);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(smtpUser));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailMessage.getTo()));
			message.setSubject(emailMessage.getSubject());
			message.setText(emailMessage.getMessage());

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
			mimeMessageHelper.setTo(emailMessage.getTo());
			mimeMessageHelper.setSubject(emailMessage.getSubject());
			String returnMessage = CreateMessage(emailMessage);
			emailMessage.setMessage(returnMessage);
			mimeMessageHelper.setText(emailMessage.getMessage(), true);
			Transport.send(message);
			System.out.println("Email Sent successfully....");

			log.info("success");

			return authNum;
		}
		catch (MessagingException e) {
			log.error(String.valueOf(e.getCause()));
			throw new RuntimeException(e);
		}
	}

	@Override
	public String createSubject(String module, String user_id, String subject_type, String email_type) {
		String subject = "";

		subject += "[" + ("doc".equals(module) ? "DMS] 문서 " : "LMS] 교육 ") + subject_type + ("sign".equals(email_type) ? " 승인 요청 안내" : " ");;
		//subject +="["+""+"] "+"doc".equals(module)?"DMS":"LMS"+"";

		return subject;
	}

	// 인증번호 및 임시 비밀번호 생성 메서드
	public String createCode() {
		Random random = new Random();
		StringBuffer key = new StringBuffer();

		for (int i = 0; i < 8; i++) {
			int index = random.nextInt(4);

			switch (index) {
			case 0:
				key.append((char) ((int) random.nextInt(26) + 97));
				break;
			case 1:
				key.append((char) ((int) random.nextInt(26) + 65));
				break;
			default:
				key.append(random.nextInt(9));
			}
		}
		return key.toString();
	}

	public String messageInfo(String module, CustomUserDetails user, String content_item, String email_type) {
		String subject = "";
		subject =

				subject += "[" + ("doc".equals(module) ? "DMS] " : "LMS] ") + content_item + ("sign".equals(module) ? email_type + " 승인 요청 안내" : email_type + " 승인 요청 안내");;
		//subject +="["+""+"] "+"doc".equals(module)?"DMS":"LMS"+"";

		return subject;
	}

	@Override
	public String messageSignInfo(PlanSignManager planSignManager, CustomUserDetails userInfo,String flag) {

		PlanSign paramSign = new PlanSign();
		String signType = planSignManager.getSign_type();
		paramSign.setSignform(signType.substring(0, 7));
		paramSign.setPlan_id(planSignManager.getPlan_id());
		PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);
		String content = "";

		if("last".equals(flag)) {
			content += "<tr><td colspan=\"2\">안녕하세요. "+resultPlanSign.getUser_nm()+" "+ resultPlanSign.getTitle()+" 결재 종료 안내 드립니다.</td></tr>" +
					"   <tr>\n" +

					"					  </tr>";
		} else {
			content += "<tr><td colspan=\"2\">안녕하세요. "+userInfo.getUser_nm()+" "+ resultPlanSign.getTitle()+" 승인 요청 안내 드립니다. </td></tr>" +
					"   <tr>\n" +

					"					  </tr>";
		}
		content+=  "						<td  colspan=\"2\" style=\"padding:20px\"></td>\n" ;

		//comi1s1 연간
		if("comi1s1".equals(planSignManager.getSignform())) {
			AnnualPlanSearchEntity paramAnnual = new AnnualPlanSearchEntity();
			paramAnnual.setId(planSignManager.getPlan_id());
			AnnualPlanDTO annualPlanDTO = annualPlanDAO.findAnnualPlan(paramAnnual);
			content += "<tr><td width=\"150\">직무의 목적</td><td>" + annualPlanDTO.getEdu_year() + "년 </td></tr>";
			content += "<tr><td width=\"150\">목적 </td><td>" + annualPlanDTO.getEdu_goal() + " </td></tr>";
			content += "<tr><td width=\"150\">적용범위 </td><td>" + annualPlanDTO.getEdu_coverage() + "</td></tr>";
			content += "<tr><td width=\"150\">역할 및 책임</td><td>" + annualPlanDTO.getRes_role() + "</td></tr> ";
		} else if("comi1s3".equals(planSignManager.getSignform())){
			ManagementPlan paramPlan =  new ManagementPlan();
			paramPlan.setId(planSignManager.getPlan_id());
			ManagementPlanDTO planDTO = managementPlanDAO.educationPlanDetail(paramPlan);
			content +="<tr><td width=\"150\">교육명</td><td>"+planDTO.getTitle()+"</td></tr>";
			content +="<tr><td width=\"150\">교육 담당자 </td><td>"+planDTO.getEdu_user_nm()+" </td></tr>";
			content +="<tr><td width=\"150\">교육 일정 </td><td>"+planDTO.getPlan_start_date()+"~"+ planDTO.getPlan_end_date()+"</td></tr>";
			content +="<tr><td width=\"150\">업무번호</td><td>"+planDTO.getWork_num()+"</td></tr> ";
			content +="<tr><td width=\"150\">교육 유형</td><td>"+planDTO.getEdu_type_nm()+"</td></tr> ";
			content +="<tr><td width=\"150\">진행 방법</td><td>"+planDTO.getProceed_type()+"</td></tr> ";
			content +="<tr><td width=\"150\">완료 기준</td><td>"+planDTO.getCompletion_type_nm()+"</td></tr> ";
			content +="<tr><td width=\"150\">평가 방법</td><td>"+planDTO.getEvaluation_type_nm()+"</td></tr> ";
			content +="<tr><td width=\"150\">재평가 횟수</td><td>"+planDTO.getRelation_num()+"</td></tr> ";
			content +="<tr><td width=\"150\">합격 기준</td><td>"+planDTO.getPassing_rate()+"</td></tr> ";

		} else if("comi1s6".equals(planSignManager.getSignform())) { // 직무 요구서 ...

			JobRevision paramJob = new JobRevision();
			paramJob.setId(planSignManager.getPlan_id());
//			paramJob.setGroup_id(planSignManager.getGroup_id());
			paramJob.setUse_flag('W');
			JobRevision resultJob = currentDAO.findJobRevision(paramJob);
			content +="<tr><td width=\"150\">직무의 목적 </td><td>"+resultJob.getPurpose()+"년 </td></tr>";
			content +="<tr><td width=\"150\">개정 사유 </td><td>"+resultJob.getReason()+" </td></tr>";
			content +="<tr><td width=\"150\">위임 여부 </td><td>"+resultJob.getMandate_nm()+"</td></tr>";
			//content +="<tr><td width=\"150\">직무명  </td><td>"+resultJob.getJob_title()+"</td></tr> ";
			content +="<tr><td width=\"150\">직무코드 </td><td>"+resultJob.getMemo()+"</td></tr> ";
		}
//		if("last".equals(flag)) {
//			content += "<tr><td colspan=\"2\" >"+planSignManager.getSign_title() + " 결재 종료 안내 </td></tr>";
//		} else {
//			content += "<tr><td colspan=\"2\">"+planSignManager.getSign_title() + " 승인 요청 안내 </td></tr>";
//		}
		content += "   <tr>\n" +
				//  "						<td height=\"1\" colspan=\"2\" style=\"background:#424240\"></td>\n" +
				"					  </tr><tr><td width=\"150\">기안일시 </td><td>"+resultPlanSign.getConvert_reg_date()+"</td></tr>";
		content += "   <tr>\n" +
				"					  </tr><tr><td width=\"150\">기안자  </td><td>"+resultPlanSign.getDept_nm()+" "+resultPlanSign.getUser_nm()+"</td></tr>";
		content+=  "						<td  colspan=\"2\" style=\"padding:20px\"></td>\n" ;

		if("last".equals(flag)) {
			CodeDTO stateCode = codeDAO.findByCodeName(planSignManager.getState());
			content += "<tr><td colspan=\"2\"> 신청된 결재건이 " + stateCode.getDiscription() + "되었습니다.</td></tr>";
			content += "<tr><td > 사유 </td> <td> " + planSignManager.getComment() + "</td></tr>";

		}
		return content;
	}

	@Override
	public String createSubject(PlanSignManager signUser, CustomUserDetails userInfo, String flag) {
		String subject = "";
		CodeDTO stateCode = codeDAO.findByCodeName(signUser.getState());
		PlanSign paramSign = new PlanSign();
		paramSign.setSignform(signUser.getSignform());
		paramSign.setPlan_id(signUser.getPlan_id());
		PlanSign resultPlanSign = signDAO.findPlanSign(paramSign);

		subject =
			subject += "["+("comc11002".equals(signUser.getSign_category()) ?"DMS] ":"LMS] ")+ resultPlanSign.getTitle()+
					("last".equals(flag) ?" 결재 건이 "+stateCode.getDiscription()+"되었습니다." : " 승인 요청 안내");
		;
		//subject +="["+""+"] "+"doc".equals(module)?"DMS":"LMS"+"";

		return subject;
	}
	public String CreateMessage(EmailMessage emailMessage) {
		String mailContent = "<!DOCTYPE html>\n" +
				"<html lang=\"ko\">\n" +
				"	<head>\n" +
				"		<meta charset=\"UTF-8\" />\n" +
				"		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
				"		<title></title>\n" +
				"	</head><body>\n" +
				"	\n" ;
		mailContent +=" <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"\n" +
				"   border: 3px solid #22B4E6; border-bottom: 3px solid #22B4E6 ;\n" +
				"  max-width:595px;width:100%;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;background-color:#fff;-webkit-text-size-adjust:100%;text-align:left\">\n" +
				"	<tbody>";
		mailContent +="<tr>\n" +
				"		<td height=\"30\"></td>\n" +
				"	  </tr>\n" +
				"	  <tr>\n" +
				"		<td style=\"padding-right:27px; padding-left:21px\">\n" +
				"		  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
				"			<tbody>\n" +
				"			  <tr>\n" +
				"				<td style=\"padding-bottom: 32px; font-size: 20px; font-weight: bold;\">\n" +
//				"				  <img width=\"92\" src=\"\">inno.N\n" +
				"				   <img width=\"92\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAcoAAAB2CAYAAABI49NmAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDkuMS1jMDAyIDc5LmE2YTYzOTY4YSwgMjAyNC8wMy8wNi0xMTo1MjowNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo3ODA0YjM2NC1iMDE2LTU3NGEtYjEyMy1hYWE2ODRmZTMzZWMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OEVGMzI1RkU0QTUzMTFFRjlBMTZBQkY5RTFCRjE2OUMiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OEVGMzI1RkQ0QTUzMTFFRjlBMTZBQkY5RTFCRjE2OUMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIDI1LjkgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6ODY1ZGI3ZDctNmJhYy1mYTRlLWI4Y2MtM2Y0YzIzZWMwZTE1IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjc4MDRiMzY0LWIwMTYtNTc0YS1iMTIzLWFhYTY4NGZlMzNlYyIv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\">" +
				"				</td>\n" +
				"			  </tr>\n" +
				"			</tbody>\n" +
				"		  </table>\n" +
				"		</td>\n" +
				"	  </tr>";
		mailContent +="<tr>\n" +
				"		<td style=\"padding-right:27px; padding-left:18px;line-height:40px;font-size:20px;color:#424240;\">\n" +
				"		  <img style=\"float: left; height: 35px\" src=\"data:image/png;base64,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\" alt=\"#\">";
		mailContent += emailMessage.getSubject();
		mailContent += "</td>\n" +
				"	  </tr>";
		mailContent +="<tr>\n" +
				"		<td style=\"padding-right:27px; padding-bottom:32px; padding-left:20px\">\n" +
				"		  <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;\">\n" +
				"			<tbody>\n" +
				"			  <tr>\n" +
				"				<td height=\"1\"></td>\n" +
				"			  </tr>\n" +
				"			  <tr>\n" +
				"				<td style=\"font-size:14px;color:#696969;line-height:24px;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;\">\n" +
				"				  <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\n" +
				"					<tbody>\n" +
				"					  <tr>\n" +
				"						<td height=\"23\" style=\"font-weight:bold;color:#000;vertical-align:top;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;\">\n" +
				"						   </td>\n" +
				"					  </tr>\n" +
				"					  <tr>\n" +
				"						<td height=\"2\" style=\"background:#424240\"></td>\n" +
				"					  </tr>\n" +
				"					  <tr>\n" +
				"						<td height=\"20\"></td>\n" +
				"					  </tr>\n" +
				"					  <tr>\n" +
				"						<td>\n" +
				"						  <table cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%;margin:0;padding:0\">\n" +
				"							<tbody>";


				mailContent += emailMessage.getMessage();






		mailContent +="</tbody>\n" +
				"						  </table>\n" +
				"						</td>\n" +
				"					  </tr>\n" +
				"					  <tr>\n" +
				"						<td height=\"20\"></td>\n" +
				"					  </tr>\n" +
				"					  <tr>\n" +
				"						<td height=\"2\" style=\"background:#424240\"></td>\n" +
				"					  </tr>\n" +
				"					</tbody>\n" +
				"				  </table>\n" +
				"				</td>\n" +
				"			  </tr>\n" +
				"			  <tr>\n" +
				"				<td height=\"24\"></td>\n" +
				"			  </tr>\n" +
				"			\n" +
				"			  <tr>\n" +
				"				<td style=\"height:34px;font-size:14px; text-align: center;color:#696969;font-family:'나눔고딕',NanumGothic,'맑은고딕',Malgun Gothic,'돋움',Dotum,Helvetica,'Apple SD Gothic Neo',Sans-serif;\">\n" +
				"				  <a href=\""+emailMessage.getUrl()+"\" \n" +
				"				  style=\"display:inline-block;padding:10px 10px 10px; margin-top:10px;\n" +
				"				   background-color:#276ef1 ; color:#fff;text-align: center; text-decoration: none;\"\n" +
				"					target=\"_blank\" rel=\"noreferrer noopener\">DIP 접속하기</a>\n" +
				"			  </td></tr>\n" +
				"			  <tr>\n" +
				"				<td height=\"24\"></td>\n" +
				"			  </tr>\n" +
				"			</tbody>\n" +
				"		  </table>\n" +
				"		</td>\n" +
				"	  </tr>\n" +
				"	  <tr>\n" +
				"		<td style=\"padding-bottom: 24px; padding-left: 24px; color: #A7A7A7; font-size: 12px; line-height: 20px;\">© All Rights Reserved. HK\n" +
				"		  inno.N Corp.</td>\n" +
				"	  </tr>\n" +
				"	</tbody>\n" +
				"  </table>"+
				"	</body>\n" +
				"</html>\n";

		return mailContent;
	}
}