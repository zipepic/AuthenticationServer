package com.example.authenticationserver.query.api.service;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void deleteUserProfileEntityById(String id) {
        userProfileRepository.deleteById(id);
    }

    @Transactional
    public void addUserProfileEntity(UserProfileEntity userProfileEntity) {
        userProfileRepository.save(userProfileEntity);
    }

    @Transactional
    public void updateUserProfileEntity(UserProfileEntity userProfileEntity) {
        userProfileRepository.save(userProfileEntity);
    }

    @Transactional
    public List<UserProfileEntity> findAllUserProfileEntity() {
        return userProfileRepository.findAll();
    }

    @Transactional
    public UserProfileEntity findUserProfileEntityById(String id) {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findById(id);
        if (userProfileEntity.isEmpty()) throw new UsernameNotFoundException("User not found");
        return userProfileEntity.get();
    }

    @Transactional
    public UserProfileEntity findUserProfileEntityByUsername(String username) {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findByUserName(username);
        if (userProfileEntity.isEmpty()) throw new UsernameNotFoundException("User not found");
        return userProfileEntity.get();
    }
    public UserProfileEntity findUserProfileEntityByGithubId(String githubId) {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findByGithubId(githubId);
        if (userProfileEntity.isEmpty()) return null;
        return userProfileEntity.get();
    }
    public UserProfileEntity findUserProfileEntityByGoogleId(String googleId) {
        Optional<UserProfileEntity> userProfileEntity = userProfileRepository.findByGoogleId(googleId);
        if (userProfileEntity.isEmpty()) return null;
        return userProfileEntity.get();
    }
}
