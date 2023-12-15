package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.data.user.UserProfileEntity;
import com.example.authenticationserver.query.api.data.user.UserProfileRepository;
import com.example.authenticationserver.query.api.service.UserProfileEntityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.jwk.JWKSet;
import com.project.core.dto.UserProfileDTO;
import com.project.core.queries.user.*;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tokenlib.util.jwk.SimpleJWK;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserProfileQueryHandler {
    private final UserProfileEntityService userProfileEntityService;

    @Autowired
    public UserProfileQueryHandler(UserProfileEntityService userProfileEntityService) {
        this.userProfileEntityService = userProfileEntityService;
    }

    @QueryHandler
    public String findUserIdByUserName(FindUserIdByUserNameQuery query) {
        try {
            return userProfileEntityService.findUserProfileEntityByUsername(query.getUserName()).getUserId();
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @QueryHandler
    public UserProfileEntity findUserProfileByUserId(FetchUserProfileByUserIdQuery query) {
        try {
            return userProfileEntityService.findUserProfileEntityByUsername(query.getUserId());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
    @QueryHandler
    public UserProfileDTO findUserProfile(FetchUserProfileDTOByUserIdQuery query) {
        try {
            var user = userProfileEntityService.findUserProfileEntityById(query.getUserId());
            var userProfileDTO = UserProfileDTO.builder().build();
            BeanUtils.copyProperties(user, userProfileDTO);
            return userProfileDTO;
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @QueryHandler
    public UserProfileEntity findUserProfileByUserName(FetchUserProfileByUserNameQuery query) {
        try {
            return userProfileEntityService.findUserProfileEntityByUsername(query.getUserName());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @QueryHandler
    public String findUserIdByProviderId(CheckUserProfileByProviderIdQuery query) {
        if(query.getProviderType().equals("github")){
            var user = userProfileEntityService.findUserProfileEntityByGithubId(query.getProviderId());
            return user == null ? null : user.getUserId();
        }else if(query.getProviderType().equals("google")){
            var user = userProfileEntityService.findUserProfileEntityByGoogleId(query.getProviderId());
            return user == null ? null : user.getUserId();
        }
        return null;
    }
}
