package com.example.authenticationserver.query.api.data.user;

import org.springframework.stereotype.Service;

@Service
public class UserProfileEntityService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileEntityService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void deleteUserProfileEntityById(String id){
        userProfileRepository.deleteById(id);
    }

    public void addUserProfileEntity(UserProfileEntity userProfileEntity){
        userProfileRepository.save(userProfileEntity);
    }
}
