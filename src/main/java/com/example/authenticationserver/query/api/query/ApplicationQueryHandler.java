package com.example.authenticationserver.query.api.query;

import com.example.authenticationserver.query.api.service.ApplicationService;
import com.project.core.queries.app.CheckLoginDataQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ApplicationQueryHandler {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationQueryHandler(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @QueryHandler
    public boolean on(CheckLoginDataQuery query) {
        try {
            return applicationService.checkPassword(query.getSecret(), query.getClientId());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Not found");
        }
    }
}
