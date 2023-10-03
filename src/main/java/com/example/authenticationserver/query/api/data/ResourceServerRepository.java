package com.example.authenticationserver.query.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceServerRepository extends JpaRepository<ResourceServerEntity,String> {
}
