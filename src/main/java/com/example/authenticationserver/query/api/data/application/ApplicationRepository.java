package com.example.authenticationserver.query.api.data.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity,String> {
}
