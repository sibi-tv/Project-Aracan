package com.aracanclothing.aracan.services.auth;

import com.aracanclothing.aracan.dto.SignupRequest;
import com.aracanclothing.aracan.dto.UserDto;

public interface AuthService {

    UserDto createUser(SignupRequest signupRequest);
    Boolean hasUserWithEmail(String email);
}
