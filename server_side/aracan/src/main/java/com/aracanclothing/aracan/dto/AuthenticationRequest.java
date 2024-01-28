package com.aracanclothing.aracan.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String user_name;
    private String password;
}
