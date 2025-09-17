package com.innon.education.auth.dto.response;


import com.innon.education.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;

}
