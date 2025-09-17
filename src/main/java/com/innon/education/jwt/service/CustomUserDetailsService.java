package com.innon.education.jwt.service;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.jwt.dto.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginRequest paramUser = new LoginRequest();
        paramUser.setUser_id(email);
        User dbUser = userRepository.findUser(paramUser);
        LOGGER.info("Fetched user : " + dbUser + " by " + email);
        return dbUser;
    }

    public UserDetails loadUserById(String id) {
        LoginRequest paramUser = new LoginRequest();
        paramUser.setUser_id(id);
        User dbUser = userRepository.findUser(paramUser);
        LOGGER.info("Fetched user : " + dbUser + " by " + id);
        return dbUser;
    }
}