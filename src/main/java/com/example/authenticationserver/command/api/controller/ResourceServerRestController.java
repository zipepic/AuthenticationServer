package com.example.authenticationserver.command.api.controller;

import com.example.authenticationserver.command.api.restmodel.ResourceServerCreateRestModel;
import com.example.authenticationserver.query.api.dto.TokenManagementDTO;
import com.project.core.commands.CreateResourceServerCommand;
import com.project.core.queries.FetchResourceServersQuery;
import com.project.core.queries.FetchTokensByTokenId;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource_server")
public class ResourceServerRestController {
  private final CommandGateway commandGateway;
  private final QueryGateway queryGateway;
  @Autowired
  public ResourceServerRestController(CommandGateway commandGateway, QueryGateway queryGateway) {
    this.commandGateway = commandGateway;
    this.queryGateway = queryGateway;
  }

  @PostMapping("/create")
  public String createResourceServer(@RequestBody ResourceServerCreateRestModel restModel){
    CreateResourceServerCommand command =
      CreateResourceServerCommand.builder()
        .resourceServerName(restModel.getResourceServerName())
        .secret(restModel.getSecret())
        .build();
    return commandGateway.sendAndWait(command);
  }
  @GetMapping("/get")
  public List getResourceServer(){
    FetchResourceServersQuery query = FetchResourceServersQuery.builder().build();

    return queryGateway.query(query, List.class).join();
  }
  @GetMapping("/token")
  public TokenManagementDTO getToken(@RequestParam String tokenId){
    FetchTokensByTokenId query = FetchTokensByTokenId.builder()
      .tokenId(tokenId)
      .build();

    return queryGateway.query(query, TokenManagementDTO.class).join();
  }
}
