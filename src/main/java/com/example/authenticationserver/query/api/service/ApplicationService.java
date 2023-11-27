package com.example.authenticationserver.query.api.service;

import com.example.authenticationserver.query.api.data.application.ApplicationEntity;
import com.example.authenticationserver.query.api.data.application.ApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;

    public ApplicationService(ApplicationRepository applicationRepository, PasswordEncoder passwordEncoder) {
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ApplicationEntity findById(String id) {
        Optional<ApplicationEntity> applicationEntity = applicationRepository.findById(id);
        if (applicationEntity.isEmpty()) throw new NoSuchElementException("Not found");
        return applicationEntity.get();
    }

    @Transactional
    public boolean checkPassword(String password, String clientId) {
        Optional<ApplicationEntity> applicationEntity = applicationRepository.findById(clientId);
        if (applicationEntity.isEmpty()) throw new NoSuchElementException("Not found");
        return passwordEncoder.matches(password, applicationEntity.get().getSecret());
    }
}
