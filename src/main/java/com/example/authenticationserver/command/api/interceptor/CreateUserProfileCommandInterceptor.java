package com.example.authenticationserver.command.api.interceptor;

import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;

public class CreateUserProfileCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {
}
