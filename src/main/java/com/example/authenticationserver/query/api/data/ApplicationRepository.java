package com.example.authenticationserver.query.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity,String> {
}
