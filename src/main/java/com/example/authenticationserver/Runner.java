package com.example.authenticationserver;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.example.authenticationserver.query.api.data.user.UserProfileEntityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Runner implements CommandLineRunner {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileEntityService userProfileEntityService;

    public Runner(UserProfileRepository userProfileRepository, UserProfileEntityService userProfileService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileEntityService = userProfileService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        UserProfileEntity userProfileEntity = new UserProfileEntity();
        userProfileEntity.setUserId(UUID.randomUUID().toString());
        userProfileEntity.setUserName("testuser2");
        userProfileEntity.setUserStatus("status");
        userProfileEntity.setRole("role");
        userProfileEntity.setPasswordHash("11111");
        userProfileEntityService.addUserProfileEntity(userProfileEntity);

    }
}
