package com.example.authenticationserver.query.api.service;

import com.example.authenticationserver.query.api.data.application.ApplicationEntity;
import com.example.authenticationserver.query.api.data.application.ApplicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
class ApplicationServiceTest {

    @MockBean
    private final ApplicationRepository applicationRepository;

    @InjectMocks
    private final ApplicationService applicationService;

    @InjectMocks
    private final PasswordEncoder passwordEncoder;

    private final ApplicationEntity applicationEntity = new ApplicationEntity();

    @Autowired
    ApplicationServiceTest(ApplicationRepository applicationRepository, ApplicationService applicationService, PasswordEncoder passwordEncoder) {
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
        this.passwordEncoder = passwordEncoder;
    }

    @BeforeEach
    public void init() {
        this.applicationEntity.setClientId("1");
        this.applicationEntity.setSecret(passwordEncoder.encode("123456"));
    }

    @Test
    void testFindByIdIfNull() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            applicationService.findById(null);
        });
    }

    @Test
    void testFindByIdIfRightId() {
        when(applicationRepository.findById("1")).thenReturn(Optional.of(applicationEntity));
        Assertions.assertEquals("1", applicationService.findById("1").getClientId());
    }

    @Test
    void testFindByIdIfWrongId() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            applicationService.findById("0");
        });
    }

    @Test
    void testCheckPasswordIfNull() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            applicationService.checkPassword(null, null);
        });
    }

    @Test
    void testCheckPasswordIfEqual() {
        String querySecret = "123456";
        when(applicationRepository.findById("1")).thenReturn(Optional.of(applicationEntity));
        Assertions.assertTrue(applicationService.checkPassword(querySecret, applicationEntity.getClientId()));
    }

    @Test
    void testCheckPasswordIfNotEqual() {
        String querySecret = "1234567";
        when(applicationRepository.findById("1")).thenReturn(Optional.of(applicationEntity));
        Assertions.assertFalse(applicationService.checkPassword(querySecret, applicationEntity.getClientId()));
    }
}