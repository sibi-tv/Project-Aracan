package com.aracanclothing.aracan.services.auth;

import com.aracanclothing.aracan.dto.SignupRequest;
import com.aracanclothing.aracan.dto.UserDto;
import com.aracanclothing.aracan.entity.User;
import com.aracanclothing.aracan.enums.UserRole;
import com.aracanclothing.aracan.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository user_repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto createUser(SignupRequest signupRequest) {
        User user = new User();

        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setRole(UserRole.CUSTOMER);

        User createdUser = user_repository.save(user);
        UserDto userDto = new UserDto();
        userDto.setId(createdUser.getId());

        return userDto;
    }

    public Boolean hasUserWithEmail(String email) {
        return user_repository.findFirstByEmail(email).isPresent();
    }

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = user_repository.findByRole(UserRole.ADMIN);
        if (adminAccount == null) {
            User admin_user = new User();
            admin_user.setEmail("sibi.tiruchi@gmail.com");
            admin_user.setName("Sibi");
            admin_user.setRole(UserRole.ADMIN);
            admin_user.setPassword(new BCryptPasswordEncoder().encode("aracan_admin"));
            user_repository.save(admin_user);
        }
    }
}
