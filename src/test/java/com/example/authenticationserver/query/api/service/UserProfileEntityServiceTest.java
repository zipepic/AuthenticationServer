package com.example.authenticationserver.query.api.service;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserProfileEntityServiceTest {

    @MockBean
    private final UserProfileRepository userProfileRepository;

    @InjectMocks
    private final UserProfileEntityService userProfileEntityService;

    private final UserProfileEntity userProfileEntity = new UserProfileEntity();

    @Autowired
    UserProfileEntityServiceTest(UserProfileRepository userProfileRepository, UserProfileEntityService userProfileEntityService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileEntityService = userProfileEntityService;
    }


    @BeforeEach
    public void init() {
        userProfileEntity.setUserId("1");
        userProfileEntity.setUserName("Vasiliy");
    }

    @Test
    public void deleteUserProfileEntityById() {
        Mockito.doNothing().when(userProfileRepository).deleteById("1");
        userProfileEntityService.deleteUserProfileEntityById("1");
        Mockito.verify(userProfileRepository, Mockito.times(1)).deleteById("1");
    }

    @Test
    public void deleteUserProfileEntityByIdIfException() {
        Mockito.doThrow(new RuntimeException("User not found")).when(userProfileRepository).deleteById("1");
        Assertions.assertThrows(RuntimeException.class, () ->
        {
            userProfileEntityService.deleteUserProfileEntityById("1");
        });
    }

    @Test
    public void addUserProfileEntity() {
        Mockito.when(userProfileRepository.save(Mockito.any(UserProfileEntity.class))).thenReturn(userProfileEntity);
        userProfileEntityService.addUserProfileEntity(userProfileEntity);
        Mockito.verify(userProfileRepository, Mockito.times(1)).save(userProfileEntity);
    }

    @Test
    public void addUserProfileEntityIfException() {
        Mockito.doThrow(new RuntimeException("Server unreachable")).when(userProfileRepository).save(userProfileEntity);
        Assertions.assertThrows(RuntimeException.class, () -> {
            userProfileEntityService.addUserProfileEntity(userProfileEntity);
        });
    }

    @Test
    public void updateUserProfileEntity() {
        Mockito.when(userProfileRepository.save(Mockito.any(UserProfileEntity.class))).thenReturn(userProfileEntity);
        userProfileEntityService.updateUserProfileEntity(userProfileEntity);
        Mockito.verify(userProfileRepository, Mockito.times(1)).save(userProfileEntity);
    }

    @Test
    public void updateUserProfileEntityIfException() {
        Mockito.doThrow(new RuntimeException("Server unreachable")).when(userProfileRepository).save(userProfileEntity);
        Assertions.assertThrows(RuntimeException.class, () -> {
            userProfileEntityService.updateUserProfileEntity(userProfileEntity);
        });
    }

    @Test
    public void findAllUserProfileEntity() {
        Mockito.when(userProfileRepository.findAll()).thenReturn(List.of(userProfileEntity));
        Assertions.assertEquals("1", userProfileEntityService.findAllUserProfileEntity().get(0).getUserId());
    }

    @Test
    public void findAllUserProfileEntityIfException() {
        Mockito.doThrow(new RuntimeException("Server unreachable")).when(userProfileRepository).findAll();
        Assertions.assertThrows(RuntimeException.class, () -> {
            userProfileEntityService.findAllUserProfileEntity();
        });
    }

    @Test
    public void findUserProfileEntityById() {
        Mockito.when(userProfileRepository.findById("1")).thenReturn(Optional.of(userProfileEntity));
        Assertions.assertEquals(userProfileEntity, userProfileEntityService.findUserProfileEntityById("1"));
    }

    @Test
    public void findUserProfileEntityByIdIfNotFound() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userProfileRepository).findById("1");
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userProfileEntityService.findUserProfileEntityById("1");
        });
    }

    @Test
    void findUserProfileEntityByUsername() {
        Mockito.when(userProfileRepository.findByUserName("Vasiliy")).thenReturn(Optional.of(userProfileEntity));
        Assertions.assertEquals(userProfileEntity, userProfileEntityService.findUserProfileEntityByUsername("Vasiliy"));
    }

    @Test
    void findUserProfileEntityByUsernameIfNotFound() {
        Mockito.doThrow(UsernameNotFoundException.class).when(userProfileRepository).findByUserName("Vasiliy");
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userProfileEntityService.findUserProfileEntityByUsername("Vasiliy");
        });
    }
}