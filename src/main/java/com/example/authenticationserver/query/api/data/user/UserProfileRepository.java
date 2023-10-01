package com.example.authenticationserver.query.api.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, String> {
  public Optional<UserProfileEntity> findByUserName(String userName);
}
