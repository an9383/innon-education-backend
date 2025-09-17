package com.innon.education.auth.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.innon.education.admin.manage.dao.ManagerDAO;
import com.innon.education.admin.manage.repository.Manager;
import com.innon.education.admin.manage.repository.ManagerUser;
import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.dto.request.RefreshToken;
import com.innon.education.auth.dto.request.RegisterRequest;
import com.innon.education.auth.dto.response.AuthenticationDto;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.auth.service.AuthenticationService;
import com.innon.education.common.dao.CommonDAO;
import com.innon.education.common.repository.model.Insa;
import com.innon.education.common.repository.model.Login;
import com.innon.education.common.util.DataLib;
import com.innon.education.jwt.dao.TokenRepository;
import com.innon.education.jwt.dto.CustomUserDetails;
import com.innon.education.jwt.dto.Token;
import com.innon.education.jwt.dto.TokenType;
import com.innon.education.jwt.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    CommonDAO commonDAO;
    @Autowired
    ManagerDAO managerDAO;


    @Override
    public AuthenticationDto login(LoginRequest request) throws BadRequestException {

        CustomUserDetails user = userRepository.findUser(request);
        User userCheck = userRepository.findUser(request);
        if(user == null) {
            throw new BadRequestException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        if(("N").equals(userCheck.getPw_reset_flag())){
            throw new BadRequestException("로그인 5회 실패로 사용 정지 되었습니다. 비밀번호 찾기 후 시도해 주세요.");
        }else if("1".equals(user.getPw_reset_flag())){
           // throw new BadRequestException("최초 로그인 입니다. 비밀번호 찾기 후 시도해 주세요.");
        }
        if(userCheck.getGroup_id() == 0 ){
            throw new BadRequestException("로그인 권한이 없습니다. 관리자에게 문의해 주세요.");
        }
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

        String chkDate = SDF.format(calendar.getTime());
        System.out.println("Today : " + chkDate);
        calendar.add(Calendar.DATE, -90);
        chkDate = SDF.format(calendar.getTime());

        int compare = userCheck.getPw_reset_date().compareTo(calendar.getTime());



        if(compare <  0 ){
           // throw new BadRequestException("비밀번호를 변경하신지 90일이 지났습니다. 비밀번호 찾기 후 시도해 주세요.");
        }


        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(user.getUse_flag() == 0) {
            throw new BadRequestException("사용 정지된 계정입니다. 관리자에게 문의해 주세요.");
        }
        if(user.getDel_flag() == 1) {
            throw new BadRequestException("퇴직한 사용자 입니다. 관리자에게 문의해 주세요.");
        }
        
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            Insa insa = new Insa();
            insa.setUser_id(request.getUser_id());
            if(user.getPw_wrong_cnt() < 5){
                insa.setPw_wrong_cnt(user.getPw_wrong_cnt()+1);
            }else{
                insa.setPw_reset_flag("N");
            }
            commonDAO.updateInsa(insa);
            throw new BadRequestException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        Insa insa = new Insa();
        insa.setUser_id(request.getUser_id());
        insa.setPw_wrong_cnt(0);
        commonDAO.updateInsa(insa);
        revokeAllUserTokens(user);
        AuthenticationDto authDto = saveUserTokenAndReturnAuthResponse(user);
        Manager manager = new Manager();
        manager.setManager_user_id(user.getUser_id());
        List<ManagerUser> managerRole = managerDAO.findManagerUserList(manager);
        authDto.setManager_info(managerRole);
        authDto.setAuth_menu(managerDAO.findAuthMenu(manager));
        authDto.setUser_role(userRepository.findUserRole(user.getUser_id()));
        return authDto;
    }

    private Authentication loginProcess(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUser_id(),
                        request.getPassword()

                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;

    }

    @Override
    public AuthenticationDto colMalogin(LoginRequest request)  throws BadRequestException {
        CustomUserDetails user = userRepository.findUser(request);
        User userCheck = userRepository.findUser(request);
        if(user == null) {
            throw new BadRequestException("존재하지 않는 계정입니다.");
        }
        if(userCheck.getGroup_id() == 0 ){
            throw new BadRequestException("로그인 권한이 없습니다. 관리자에게 문의해 주세요.");
        }


        revokeAllUserTokens(user);
        AuthenticationDto authDto = saveUserTokenAndReturnAuthResponse(user);
        Manager manager = new Manager();
        manager.setManager_user_id(user.getUser_id());
        List<ManagerUser> managerRole = managerDAO.findManagerUserList(manager);
        authDto.setManager_info(managerRole);
        authDto.setAuth_menu(managerDAO.findAuthMenu(manager));
        return authDto;
    }


    @Override
    public AuthenticationDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("User with email [" + request.getEmail() + "] already exists.");

        CustomUserDetails user = userRepository.save(buildUser(request));
        AuthenticationDto authDto = saveUserTokenAndReturnAuthResponse(user);
        Manager manager = new Manager();
        manager.setManager_user_id(user.getUser_id());
        List<ManagerUser> managerRole = managerDAO.findManagerUserList(manager);
        authDto.setManager_info(managerRole);
        return authDto;
    }

    @Override
    public AuthenticationDto refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {


        final String requestPath = request.getServletPath();
        final String authHeader = request.getHeader("Authorization");

        String refreshToken = request.getParameter("refresh_token");
        String userEmail =""; // username

        try {
            refreshToken = authHeader.substring(7);
            userEmail = jwtService.extractUsername(refreshToken);

            LoginRequest request1 = new LoginRequest();
            if (userEmail != null) {
                request1.setUser_id(userEmail);
                CustomUserDetails user = userRepository.findUser(request1);

                boolean isRefreshTokenValid = jwtService.isTokenValid(refreshToken, user);

                if (isRefreshTokenValid) {
                    revokeAllUserTokens(user);

                    AuthenticationDto authDto = saveUserTokenAndReturnAuthResponse(user);
                    Manager manager = new Manager();
                    manager.setManager_user_id(user.getUser_id());
                    List<ManagerUser> managerRole = managerDAO.findManagerUserList(manager);
                    authDto.setManager_info(managerRole);

                    return authDto;
                }


            }


        } catch (ExpiredJwtException e) {
           return null;
        } catch (Exception e){
            return null;
        }


        return null;
    }



    private User buildUser(RegisterRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
       //todo 정주원 password encode 잠시 주석
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPassword(request.getPassword());
//        user.getRoles(request.get);
        return user;
    }

    private AuthenticationDto saveUserTokenAndReturnAuthResponse(CustomUserDetails user) {
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        insertToken(user, jwtToken, refreshToken);
        Login login = new Login();
        login.setUser_id(user.getUser_id());
        login.setLogin_type("login");
        commonDAO.saveLoginInfo(login);

        Insa updateInsa = new Insa();
        updateInsa.setUser_id(user.getUser_id());
        Date nDate = new Date();
        updateInsa.setLast_login_date(nDate);
        commonDAO.updateInsa(updateInsa);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm:ss");
        simpleDateFormat.format(nDate);
        return AuthenticationDto.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .plant_cd(user.getPlant_cd())
                .group_id(user.getGroup_id())
                .user_nm(user.getUser_nm())
                .user_name(user.getUsername())
                .dept_nm(user.getDept_nm())
                .loginDateStr(simpleDateFormat.format(nDate))
                .dept_cd(user.getDept_cd())
                .build();
    }

    private void insertToken(User user, String jwtToken, String refreshToken) {

        Token token = new Token();
        token.setExpired(false);
        token.setRevoked(false);
        token.setToken_type(TokenType.BEARER);
        token.setRefresh_token(refreshToken);
        token.setToken(jwtToken);
        token.setUser_id(user.getUser_id());


        tokenRepository.insertToken(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokensList = tokenRepository.findTokens(user.getUser_id());

        if (DataLib.isEmpty(validUserTokensList)) return;

        validUserTokensList.forEach(t -> {
            t.setRevoked(true);
            t.setExpired(true);
            int updFlag = tokenRepository.updateToken(t);
        });


    }

    private String getRefreshTokenFromRequestBody(ServletInputStream stream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RefreshToken myRefreshToken = objectMapper.readValue(stream, RefreshToken.class);
        return myRefreshToken.getRefreshToken();
    }
}
