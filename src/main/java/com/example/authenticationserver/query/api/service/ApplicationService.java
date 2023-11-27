package com.example.authenticationserver.query.api.service;

import com.example.authenticationserver.query.api.data.application.ApplicationEntity;
import com.example.authenticationserver.query.api.data.application.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public ApplicationEntity findById(String id){
        Optional<ApplicationEntity> applicationEntity = applicationRepository.findById(id);
        if (applicationEntity.isEmpty()) throw new NoSuchElementException("Not found");
        return applicationEntity.get();
    }
}
