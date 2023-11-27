package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.service.ApplicationService;
import com.project.core.queries.app.CheckLoginDataQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ApplicationQueryHandler {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationQueryHandler(PasswordEncoder passwordEncoder, ApplicationService applicationService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationService = applicationService;
    }

    @QueryHandler
    public boolean on(CheckLoginDataQuery query) {
        try {
            return passwordEncoder.matches(query.getSecret(), applicationService.findById(query.getClientId()).getSecret());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Not found");
        }
    }
}
