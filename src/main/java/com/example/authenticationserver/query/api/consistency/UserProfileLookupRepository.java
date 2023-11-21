package com.example.authenticationserver.query.api.consistency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileLookupRepository extends JpaRepository<UserProfileLookupEntity,String> {
  Optional<UserProfileLookupEntity> findByUserIdOrUserName(String userId, String userName);
}
