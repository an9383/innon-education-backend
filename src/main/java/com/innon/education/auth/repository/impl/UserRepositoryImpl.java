package com.innon.education.auth.repository.impl;

import com.innon.education.auth.dto.request.LoginRequest;
import com.innon.education.auth.entity.Role;
import com.innon.education.auth.entity.User;
import com.innon.education.auth.repository.UserRepository;
import com.innon.education.jwt.dto.CustomUserDetails;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    SqlSessionTemplate sqlsession;

    @Override
    public CustomUserDetails findUser(LoginRequest userDTO) {

        CustomUserDetails user = new CustomUserDetails();
        try {
            user = sqlsession.selectOne("com.innon.education.auth.mapper.UserMapper.findUser",userDTO);
            if(user == null || user.getUser_id() == null){
                return user;
            }

            user.setAuthorities(loadUserAuthorities(user.getUser_id()));
        }catch (MyBatisSystemException e) {
            System.out.println(e.getCause().toString());
            System.out.println("유저가 존재하지 않습니다.");
            throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
        }

        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public CustomUserDetails save(User user) {
        return null;
    }

    @Override
    public Optional<Object> findByEmail(String username) {
        return Optional.empty();
    }

    @Override
    public List<Role> findRoleUserIdByName(String name) {
        List<Role> roleList = new ArrayList<>();
        try {
            roleList = sqlsession.selectList("com.innon.education.auth.mapper.RoleMapper.findRoleUserIdByName", name);
        } catch(MyBatisSystemException e) {
            System.out.println(e.getCause());
        }

        return roleList;
    }
    public ArrayList<GrantedAuthority> loadUserAuthorities(String user_id) {

        List<String> authorities = sqlsession.selectList("com.innon.education.auth.mapper.RoleMapper.loadUserAuthorities",user_id);
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (String auth: authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth));
        }

        return grantedAuthorities;
    }

    public List<String> findUserRole(String user_id) {
        return sqlsession.selectList("com.innon.education.auth.mapper.RoleMapper.loadUserAuthorities",user_id);
    }
}
