package com.innon.education.common.service;

import com.innon.education.admin.system.sign.repository.PlanSignManager;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.jwt.dto.CustomUserDetails;

public interface EmailService {
	String sendMail(String userId, PlanSignManager planSignManager, String signStatus, String returnUrl);

	String sendMail(EmailMessage emailMessage, String type);

	String createSubject(String module, String user_id, String subject_type, String email_type);

	String messageInfo(String module, CustomUserDetails user, String content_item, String email_type);

	String messageSignInfo(PlanSignManager planSignManager, CustomUserDetails userInfo, String flag);

	String createSubject(PlanSignManager signUser, CustomUserDetails userInfo, String last);
}
