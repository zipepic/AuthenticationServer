package com.example.authenticationserver.query.api.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, String> {
}