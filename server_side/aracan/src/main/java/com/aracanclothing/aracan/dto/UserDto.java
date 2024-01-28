package com.aracanclothing.aracan.dto;

import com.aracanclothing.aracan.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private UserRole userRole;
}
