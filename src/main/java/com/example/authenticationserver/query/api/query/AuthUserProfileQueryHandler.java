package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.service.UserProfileEntityService;
import com.project.core.queries.user.FindUserIdByUserNameAndValidatePasswordQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthUserProfileQueryHandler {
    private final UserProfileEntityService userProfileEntityService;
    private final PasswordEncoder passwordEncoder;

    public AuthUserProfileQueryHandler(UserProfileEntityService userProfileEntityService, PasswordEncoder passwordEncoder) {
        this.userProfileEntityService = userProfileEntityService;
        this.passwordEncoder = passwordEncoder;
    }
    @QueryHandler
    public String findUserIdByUserNameAndValidatePassword(FindUserIdByUserNameAndValidatePasswordQuery query) {
        try {
            var user = userProfileEntityService.findUserProfileEntityByUsername(query.getUserName());
            if(passwordEncoder.matches(query.getPassword(), user.getPasswordHash())){
                return user.getUserId();
            }
            throw new Exception("Password not match");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
