package com.innon.education.auth.repository;


import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.Role;
import com.innon.education.auth.entity.User;
import com.innon.education.jwt.dto.CustomUserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    CustomUserDetails findUser(LoginRequest userDTO);

    boolean existsByEmail(String email);

    CustomUserDetails save(User user);

    Optional<Object> findByEmail(String username);

    List<Role> findRoleUserIdByName(String name);

    List<String> findUserRole(String user_id);
}
