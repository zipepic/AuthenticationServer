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
        Assertions.assertThrows(RuntimeException.class, () -> {
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
    void updateUserProfileEntity() {
    }

    @Test
    void findAllUserProfileEntity() {
    }

    @Test
    void findUserProfileEntityById() {
    }

    @Test
    void findUserProfileEntityByUsername() {
    }
}