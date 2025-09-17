package com.innon.education.common.controller;

import com.innon.education.common.repository.dto.EmailPostDTO;
import com.innon.education.common.repository.dto.EmailResponseDTO;
import com.innon.education.common.repository.model.EmailMessage;
import com.innon.education.common.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/email")
public class EmailController {
    @Autowired
    EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity sendToMail(@RequestBody EmailPostDTO emailPostDTO) {
        EmailMessage emailMessage = EmailMessage.builder()
                .to(emailPostDTO.getEmail())
                .subject(emailPostDTO.getSubject())
                .message(emailPostDTO.getMessage())
                .build();

        String code = emailService.sendMail(emailMessage, "email");

        EmailResponseDTO emailResponseDTO = new EmailResponseDTO();
        emailResponseDTO.setCode(code);

        return ResponseEntity.ok(emailResponseDTO);
    }
}
