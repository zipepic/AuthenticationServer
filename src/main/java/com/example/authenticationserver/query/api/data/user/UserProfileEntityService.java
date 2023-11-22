package com.example.authenticationserver.query.api.data.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileEntityService {
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileEntityService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void deleteUserProfileEntityById(String id) {
        userProfileRepository.deleteById(id);
    }

    public void addUserProfileEntity(UserProfileEntity userProfileEntity) {
        userProfileRepository.save(userProfileEntity);
    }

    public void updateUserProfileEntity(UserProfileEntity userProfileEntity) {
        userProfileRepository.save(userProfileEntity);
    }

    public List<UserProfileEntity> findAllUserProfileEntity() {
        return userProfileRepository.findAll();
    }

    public UserProfileEntity findUserProfileEntityById(String id) {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findById(id);
        if (userProfileEntity.isEmpty()) throw new UsernameNotFoundException("User not found");
        return userProfileEntity.get();
    }
}
