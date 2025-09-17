package com.innon.education.auth.controller;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.dto.request.RegisterRequest;
import com.innon.education.auth.dto.response.AuthenticationDto;
import com.innon.education.auth.service.AuthenticationService;
import com.innon.education.common.service.CommonService;
import com.innon.education.controller.dto.ResultDTO;
import com.innon.education.sso.CipherDec;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authenticationService;
    private final View error;

    @Autowired
    private CommonService commonService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@RequestBody LoginRequest request) {

        try {
            return ResponseEntity.ok(authenticationService.login(request));
        } catch (BadRequestException e) {
            AuthenticationDto errorResponse = new AuthenticationDto();
            errorResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/colma_login")
    public ResponseEntity<AuthenticationDto> colMalogin(HttpServletRequest req, HttpServletResponse response,@RequestBody LoginRequest request) {

        try {
            LoginRequest loginRequest = new LoginRequest();

            String keyKolma = "HCKOLMARKEY";
            String encryptedTextKolma =request.getUser_id(); 	// 인코딩된 아이디 혹은 사번을 셋팅하세
            //sum_count

            String sum_view_flag =(req.getParameter("sum_view_flag")==null)?"":req.getParameter("sum_view_flag");	// 콜마 포탈 통합결재 카운트 여부
            String sum_view_page =(req.getParameter("sum_view_page")==null)?"":req.getParameter("sum_view_page");	// 콜마 포탈 통합결재 페이지

            //System.out.println("encrypted : " + encryptedText + "<br><br>");
            System.out.println("key : " + keyKolma + "<br><br>");
            System.out.println("encrypted : " + encryptedTextKolma + "<br><br>");
            System.out.println("sum_view_flag : " + sum_view_flag + "<br><br>");
            System.out.println("sum_view_page : " + sum_view_page + "<br><br>");

            String decryptedResult = null;
log.info("decryptedResult ===="+decryptedResult);
//            if(!"".equals(encryptedText)){
//          ????
//            }
            CipherDec cipherdec = new CipherDec();
            decryptedResult = cipherdec.decrypt(encryptedTextKolma, keyKolma);
            loginRequest.setUser_id(decryptedResult);



            return ResponseEntity.ok(authenticationService.colMalogin(loginRequest));
        } catch (BadRequestException e) {


            ResponseEntity<AuthenticationDto> status = (ResponseEntity<AuthenticationDto>) ResponseEntity.status(400);
            return status;
        } catch (IOException e) {
            System.out.println(e.getCause());
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println(e.getCause());
            ResponseEntity<AuthenticationDto> status = (ResponseEntity<AuthenticationDto>) ResponseEntity.status(404);
            return status;
        }
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto> register(@RequestBody RegisterRequest request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationDto> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationDto authenticationDto = authenticationService.refreshToken(request, response);



        if(authenticationDto != null) {
            return ResponseEntity.ok(authenticationDto);
        }else{
            return ResponseEntity.status(403).body(authenticationDto);
        }
    }
    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest servletRequest) {
      //  loginService.logout();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send/otp")
    public ResponseEntity<ResultDTO> sendOtp(@RequestBody LoginRequest login, HttpServletRequest request) {
            ResultDTO res = commonService.sendOtp(login, request);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/certi/check")
    public ResponseEntity<ResultDTO> certiCheck(@RequestBody LoginRequest login) {
        ResultDTO res = commonService.certiCheck(login);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/change/password")
    public ResponseEntity<ResultDTO> changePassword(@RequestBody LoginRequest login) {
        ResultDTO res = commonService.changePassword(login);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

}
