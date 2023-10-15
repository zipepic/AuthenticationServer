package com.example.authenticationserver.command.api.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwksRepository extends JpaRepository<JwksEntity,String> {
}
